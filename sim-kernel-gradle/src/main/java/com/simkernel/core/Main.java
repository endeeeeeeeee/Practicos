package com.simkernel.core;

import com.simkernel.memory.Memoria;
import com.simkernel.process.Proceso;
import com.simkernel.concurrency.Semaforo;

import com.simkernel.core.EstrategiaMemoria;
import com.simkernel.ui.SimViewController;

public class Main {

    public static void main(String[] args) {
        EstrategiaMemoria estrategia = EstrategiaMemoria.FIRST_FIT; // o BEST_FIT
        SistemaOperativo sistema = new SistemaOperativo(3, estrategia);
        Memoria memoria = new Memoria(512);
        Semaforo semaforo = new Semaforo();

        sistema.crearProceso("Editor", 5, 100, 1);
        sistema.crearProceso("Compilador", 8, 150, 2);
        sistema.crearProceso("Navegador", 4, 80, 3);
        sistema.crearProceso("Terminal", 6, 90, 1);
        sistema.crearProceso("Updater", 3, 70, 2);

        System.out.println("\n== Iniciando ejecución ==");

        while (sistema.getPlanificador().tieneProcesos()) {
            Proceso actual = sistema.getPlanificador().obtenerSiguiente();

            if (actual.getEstado() == Proceso.Estado.LISTO) {
                semaforo.waitSemaphore(actual);
                if (actual.getEstado() != Proceso.Estado.BLOQUEADO) {
                    actual.setEstado(Proceso.Estado.EJECUTANDO);
                    System.out.println("⚙️ Ejecutando " + actual.getNombre());
                    try { Thread.sleep(500); } catch (InterruptedException ignored) {}

                    actual.setTiempoRestante(actual.getTiempoRestante() - 1);

                    if (actual.getTiempoRestante() <= 0) {
                        actual.setEstado(Proceso.Estado.TERMINADO);
                        memoria.liberarMemoria(actual);
                        System.out.println("✅ Finalizado: " + actual.getNombre());
                    } else {
                        actual.setEstado(Proceso.Estado.LISTO);
                        sistema.getPlanificador().agregarProceso(actual);
                    }

                    semaforo.signalSemaphore();
                }
            }

            if (semaforo.hayBloqueados()) {
                Proceso desbloqueado = semaforo.signalSemaphore();
                if (desbloqueado != null)
                    sistema.getPlanificador().agregarProceso(desbloqueado);
            }
        }

        System.out.println("== Simulación completa ==");
        memoria.mostrarEstado();
    }
}
