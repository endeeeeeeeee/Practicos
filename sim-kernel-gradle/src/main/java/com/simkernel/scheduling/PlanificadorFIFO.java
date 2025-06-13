package com.simkernel.scheduling;

import com.simkernel.process.Proceso;
import java.util.LinkedList;
import java.util.Queue;

public class PlanificadorFIFO implements Planificador {
    private final Queue<Proceso> cola = new LinkedList<>();

    @Override
    public void agregarProceso(Proceso proceso) {
        cola.offer(proceso);
    }

    @Override
    public Proceso obtenerSiguiente() {
        return cola.poll();
    }

    @Override
    public boolean tieneProcesos() {
        return !cola.isEmpty();
    }
}
