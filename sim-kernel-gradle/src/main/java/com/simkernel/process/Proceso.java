// Clase base para Proceso

package com.simkernel.process;

public class Proceso {
    public enum Estado {
        NUEVO, LISTO, EJECUTANDO, BLOQUEADO, TERMINADO
    }

    private final int pid;
    private final String nombre;
    private Estado estado;
    private int tiempoRestante;
    private int memoriaNecesaria;
    private int prioridad;

    public Proceso(int pid, String nombre, int tiempoRestante, int memoriaNecesaria, int prioridad) {
        this.pid = pid;
        this.nombre = nombre;
        this.estado = Estado.NUEVO;
        this.tiempoRestante = tiempoRestante;
        this.memoriaNecesaria = memoriaNecesaria;
        this.prioridad = prioridad;
    }

    // Getters y Setters
    public int getPid() { return pid; }
    public String getNombre() { return nombre; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
    public int getTiempoRestante() { return tiempoRestante; }
    public void setTiempoRestante(int tiempoRestante) { this.tiempoRestante = tiempoRestante; }
    public int getMemoriaNecesaria() { return memoriaNecesaria; }
    public int getPrioridad() { return prioridad; }

    @Override
    public String toString() {
        return "[PID=" + pid + ", Estado=" + estado + ", Nombre=" + nombre + "]";
    }

    public int getTiempoCpu() {
        return tiempoRestante;
    }

}
