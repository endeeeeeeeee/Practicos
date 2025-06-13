// Clase base para Semaforo
package com.simkernel.concurrency;

import com.simkernel.process.Proceso;

import java.util.LinkedList;
import java.util.Queue;

public class Semaforo {

    private boolean disponible = true;
    private final Queue<Proceso> colaBloqueados = new LinkedList<>();

    // Simula la operación WAIT (P)
    public synchronized void waitSemaphore(Proceso proceso) {
        if (disponible) {
            disponible = false;
            System.out.println("🔐 " + proceso.getNombre() + " entra a sección crítica.");
        } else {
            proceso.setEstado(Proceso.Estado.BLOQUEADO);
            colaBloqueados.add(proceso);
            System.out.println("⛔ " + proceso.getNombre() + " bloqueado esperando el recurso.");
        }
    }

    // Simula la operación SIGNAL (V)
    public synchronized Proceso signalSemaphore() {
        if (!colaBloqueados.isEmpty()) {
            Proceso desbloqueado = colaBloqueados.poll();
            desbloqueado.setEstado(Proceso.Estado.LISTO);
            System.out.println("✅ " + desbloqueado.getNombre() + " desbloqueado.");
            return desbloqueado;
        } else {
            disponible = true;
            System.out.println("🔓 Sección crítica liberada.");
            return null;
        }
    }

    public boolean hayBloqueados() {
        return !colaBloqueados.isEmpty();
    }
}
