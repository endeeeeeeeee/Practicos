package com.simkernel.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simkernel.core.SistemaOperativo;
import com.simkernel.io.KernelLogger;


import java.io.File;
import java.io.InputStream;

public class ProcesoLoader {

    public static void cargarDesdeJson(SistemaOperativo sistema, String ruta) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream input = ProcesoLoader.class.getClassLoader().getResourceAsStream(ruta);
            JsonNode root = mapper.readTree(input);

            for (JsonNode nodo : root.get("procesos")) {
                String nombre = nodo.get("nombre").asText();
                int tiempo = nodo.get("tiempo").asInt();
                int memoria = nodo.get("memoria").asInt();
                int prioridad = nodo.get("prioridad").asInt();

                sistema.crearProceso(nombre, tiempo, memoria, prioridad);
                KernelLogger.LOG.   info("Cargado desde JSON: " + nombre);
            }

        } catch (Exception e) {
            KernelLogger.LOG.error("Error al cargar procesos desde JSON", e);
        }
    }
}
