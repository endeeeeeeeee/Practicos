// Clase base para Planificador

package com.simkernel.scheduling;

import com.simkernel.process.Proceso;

public interface Planificador {
    void agregarProceso(Proceso proceso);
    Proceso obtenerSiguiente();
    boolean tieneProcesos();
}
