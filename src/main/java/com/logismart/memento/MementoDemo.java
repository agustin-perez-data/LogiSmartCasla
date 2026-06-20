package com.logismart.memento;

import com.logismart.dominio.Envio;

public class MementoDemo {

    public static void main(String[] args) {
        HistorialEnvios historial = new HistorialEnvios();
        Envio envio = new Envio.EnvioBuilder("E001", "Buenos Aires", "Cordoba")
                .peso(5.0)
                .costo(150.0)
                .metodoPago("TARJETA")
                .productoId("PROD-001")
                .build();

        System.out.println("=== Caso 1: guardar ciclo de vida ===");
        historial.guardarEstado(envio);
        envio.cambiarEstado("EN_PREPARACION");
        historial.guardarEstado(envio);
        envio.cambiarEstado("EN_TRANSITO");
        historial.guardarEstado(envio);
        envio.cambiarEstado("ENTREGADO");
        historial.guardarEstado(envio);
        historial.mostrarHistorial();

        System.out.println("\n=== Caso 2: retroceder ===");
        historial.irAlEstadoAnterior(envio);
        System.out.println("Estado actual: " + envio.obtenerEstado());

        System.out.println("\n=== Caso 3: retroceder al inicio ===");
        historial.irAlEstadoAnterior(envio);
        historial.irAlEstadoAnterior(envio);
        historial.irAlEstadoAnterior(envio);
        System.out.println("Estado actual: " + envio.obtenerEstado());

        System.out.println("\n=== Caso 4: avanzar ===");
        historial.irAlEstadoSiguiente(envio);
        historial.irAlEstadoSiguiente(envio);
        System.out.println("Estado actual: " + envio.obtenerEstado());

        System.out.println("\n=== Caso 5: nuevo estado descarta futuro ===");
        System.out.println("Tamano antes: " + historial.obtenerTamano());
        envio.cambiarEstado("DEVUELTO");
        historial.guardarEstado(envio);
        System.out.println("Tamano despues: " + historial.obtenerTamano());
        historial.irAlEstadoSiguiente(envio);
    }
}
