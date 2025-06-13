// Clase base para Memoria
package com.simkernel.memory;

import com.simkernel.process.Proceso;

import java.util.ArrayList;
import java.util.List;

public class Memoria {

    private static class Bloque {
        int inicio;
        int tamanio;
        boolean libre;
        Proceso asignado;

        Bloque(int inicio, int tamanio) {
            this.inicio = inicio;
            this.tamanio = tamanio;
            this.libre = true;
        }

        @Override
        public String toString() {
            return "[Inicio=" + inicio + ", Tamaño=" + tamanio +
                    ", Libre=" + libre + (asignado != null ? ", PID=" + asignado.getPid() : "") + "]";
        }
    }

    private final List<Bloque> bloques;
    private final int totalMemoria;

    public Memoria(int totalMemoria) {
        this.totalMemoria = totalMemoria;
        this.bloques = new ArrayList<>();
        this.bloques.add(new Bloque(0, totalMemoria)); // toda la memoria como un solo bloque
    }

    public boolean asignarMemoria(Proceso proceso) {
        int requerido = proceso.getMemoriaNecesaria();

        for (int i = 0; i < bloques.size(); i++) {
            Bloque bloque = bloques.get(i);
            if (bloque.libre && bloque.tamanio >= requerido) {
                // Particiona si sobra
                if (bloque.tamanio > requerido) {
                    Bloque nuevo = new Bloque(bloque.inicio + requerido, bloque.tamanio - requerido);
                    bloques.add(i + 1, nuevo);
                }
                bloque.tamanio = requerido;
                bloque.libre = false;
                bloque.asignado = proceso;
                return true;
            }
        }
        return false;
    }

    public void liberarMemoria(Proceso proceso) {
        for (Bloque bloque : bloques) {
            if (!bloque.libre && bloque.asignado != null && bloque.asignado.getPid() == proceso.getPid()) {
                bloque.libre = true;
                bloque.asignado = null;
            }
        }
        fusionarBloquesLibres();
    }

    private void fusionarBloquesLibres() {
        for (int i = 0; i < bloques.size() - 1; ) {
            Bloque actual = bloques.get(i);
            Bloque siguiente = bloques.get(i + 1);
            if (actual.libre && siguiente.libre) {
                actual.tamanio += siguiente.tamanio;
                bloques.remove(i + 1);
            } else {
                i++;
            }
        }
    }


    public void mostrarEstado() {
        System.out.println(">> Estado de la memoria:");
        for (Bloque b : bloques) {
            System.out.println(b);
        }
    }
    // Dentro de la clase Memoria

    public boolean asignarFirstFit(Proceso proceso) {
        return asignar(proceso, this::buscarBloqueFirstFit);
    }

    public boolean asignarBestFit(Proceso proceso) {
        return asignar(proceso, this::buscarBloqueBestFit);
    }

    private interface EstrategiaBusqueda {
        int buscar(int requerido);
    }

    private boolean asignar(Proceso proceso, EstrategiaBusqueda estrategia) {
        int requerido = proceso.getMemoriaNecesaria();
        int index = estrategia.buscar(requerido);
        if (index == -1) return false;

        Bloque bloque = bloques.get(index);

        if (bloque.tamanio > requerido) {
            Bloque nuevo = new Bloque(bloque.inicio + requerido, bloque.tamanio - requerido);
            bloques.add(index + 1, nuevo);
        }

        bloque.tamanio = requerido;
        bloque.libre = false;
        bloque.asignado = proceso;
        return true;
    }

    private int buscarBloqueFirstFit(int requerido) {
        for (int i = 0; i < bloques.size(); i++) {
            if (bloques.get(i).libre && bloques.get(i).tamanio >= requerido)
                return i;
        }
        return -1;
    }

    private int buscarBloqueBestFit(int requerido) {
        int bestIndex = -1;
        int bestSize = Integer.MAX_VALUE;
        for (int i = 0; i < bloques.size(); i++) {
            Bloque b = bloques.get(i);
            if (b.libre && b.tamanio >= requerido && b.tamanio < bestSize) {
                bestIndex = i;
                bestSize = b.tamanio;
            }
        }
        return bestIndex;
    }


}
