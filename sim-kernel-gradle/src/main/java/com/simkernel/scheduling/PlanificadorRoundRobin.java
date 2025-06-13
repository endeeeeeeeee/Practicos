package com.simkernel.scheduling;

import com.simkernel.process.Proceso;
import java.util.LinkedList;
import java.util.Queue;

public class PlanificadorRoundRobin implements Planificador {
    private final Queue<Proceso> cola = new LinkedList<>();
    private final int quantum;

    public PlanificadorRoundRobin(int quantum) {
        this.quantum = quantum;
    }

    @Override
    public void agregarProceso(Proceso proceso) {
        cola.offer(proceso);
    }

    @Override
    public Proceso obtenerSiguiente() {
        Proceso actual = cola.poll();
        if (actual != null && actual.getTiempoRestante() > quantum) {
            actual.setTiempoRestante(actual.getTiempoRestante() - quantum);
            cola.offer(actual);  // vuelve al final
        } else if (actual != null) {
            actual.setTiempoRestante(0);
            actual.setEstado(Proceso.Estado.TERMINADO);
        }
        return actual;
    }

    @Override
    public boolean tieneProcesos() {
        return !cola.isEmpty();
    }
}
