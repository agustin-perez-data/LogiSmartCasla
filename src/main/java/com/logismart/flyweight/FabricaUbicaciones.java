package com.logismart.flyweight;

import java.util.HashMap;
import java.util.Map;

public class FabricaUbicaciones {
    private static final Map<String, Ubicacion> ubicaciones = new HashMap<>();

    public static Ubicacion obtener(String ciudad, String provincia, String codigoPostal) {
        String clave = ciudad + "|" + provincia + "|" + codigoPostal;
        if (!ubicaciones.containsKey(clave)) {
            Ubicacion nueva = new Ubicacion(ciudad, provincia, codigoPostal);
            ubicaciones.put(clave, nueva);
            System.out.println("  [Flyweight] Nueva ubicación creada: " + clave);
        } else {
            System.out.println("  [Flyweight] Reutilizando ubicación: " + clave);
        }
        return ubicaciones.get(clave);
    }

    public static int obtenerCantidadUnicas() {
        return ubicaciones.size();
    }

    public static void mostrarEstadisticas() {
        System.out.println("\n=== Estadísticas Flyweight ===");
        System.out.println("Ubicaciones únicas en memoria: " + ubicaciones.size());
        for (String clave : ubicaciones.keySet()) {
            System.out.println("  - " + clave);
        }
    }

    public static void limpiar() {
        ubicaciones.clear();
    }
}
