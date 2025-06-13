// Clase base para SistemaOperativo
package com.simkernel.core;


import com.simkernel.memory.Memoria;

import com.simkernel.process.Proceso;
import com.simkernel.scheduling.Planificador;
import com.simkernel.scheduling.PlanificadorRoundRobin;

import java.util.ArrayList;
import java.util.List;

public class SistemaOperativo {
    private final List<Proceso> procesos;
    private final Planificador planificador;

    private final Memoria memoria;
    private final EstrategiaMemoria estrategiaMemoria;


    public SistemaOperativo(int quantum, EstrategiaMemoria estrategiaMemoria) {
        this.procesos = new ArrayList<>();
        this.planificador = new PlanificadorRoundRobin(quantum);
        this.estrategiaMemoria = estrategiaMemoria;
        this.memoria = new Memoria(512); // Total de memoria simulada
    }

    public void crearProceso(String nombre, int tiempo, int memoriaReq, int prioridad) {
        int pid = procesos.size() + 1;
        Proceso nuevo = new Proceso(pid, nombre, tiempo, memoriaReq, prioridad);

        boolean asignado = switch (estrategiaMemoria) {
            case FIRST_FIT -> memoria.asignarFirstFit(nuevo);
            case BEST_FIT -> memoria.asignarBestFit(nuevo);
        };

        if (!asignado) {
            System.out.println("❌ No se pudo asignar memoria a " + nombre + " (PID=" + pid + ")");
            return;
        }

        nuevo.setEstado(Proceso.Estado.LISTO);
        procesos.add(nuevo);
        planificador.agregarProceso(nuevo);
        System.out.println("[+] Proceso creado: " + nuevo);
    }


    public void ejecutarSimulacion() {
        System.out.println("== Iniciando Simulación ==");
        while (planificador.tieneProcesos()) {
            Proceso actual = planificador.obtenerSiguiente();
            if (actual != null && actual.getEstado() != Proceso.Estado.TERMINADO) {
                actual.setEstado(Proceso.Estado.EJECUTANDO);
                System.out.println(">> Ejecutando: " + actual);
                // Simula ejecución
                try { Thread.sleep(500); } catch (InterruptedException ignored) {}
                if (actual.getTiempoRestante() == 0) {
                    actual.setEstado(Proceso.Estado.TERMINADO);
                    System.out.println("✔️ Proceso terminado: " + actual.getNombre());
                } else {
                    actual.setEstado(Proceso.Estado.LISTO);
                    planificador.agregarProceso(actual);
                }
            }
        }
        System.out.println("== Simulación finalizada ==");
    }
    public List<Proceso> getProcesos() {
        return procesos;
    }

    public Planificador getPlanificador() {
        return planificador;
    }
    public boolean simularPaso() {
        if (!planificador.tieneProcesos()) return false;

        Proceso actual = planificador.obtenerSiguiente();

        if (actual.getEstado() == Proceso.Estado.LISTO) {
            actual.setEstado(Proceso.Estado.EJECUTANDO);
            actual.setTiempoRestante(actual.getTiempoRestante() - 1);

            if (actual.getTiempoRestante() <= 0) {
                actual.setEstado(Proceso.Estado.TERMINADO);
                memoria.liberarMemoria(actual); // 🔑 aquí liberamos
            } else {
                actual.setEstado(Proceso.Estado.LISTO);
                planificador.agregarProceso(actual);
            }

        }

        return planificador.tieneProcesos();
    }

    public double calcularUsoMemoria() {
        int total = 512;
        int usado = 0;
        for (Proceso p : procesos) {
            if (p.getEstado() != Proceso.Estado.TERMINADO) {
                usado += p.getMemoriaNecesaria();
            }
        }
        return (double) usado / total;
    }


}
