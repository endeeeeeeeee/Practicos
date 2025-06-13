// com.simkernel.security.Usuario.java
package com.simkernel.security;

public class Usuario {
    private final String nombre;
    private final int permisos; // Bitmask: 1=READ, 2=WRITE, 4=EXEC

    public Usuario(String nombre, int permisos) {
        this.nombre = nombre;
        this.permisos = permisos;
    }

    public boolean tienePermiso(int permiso) {
        return (permisos & permiso) != 0;
    }

    public String getNombre() {
        return nombre;
    }
}
