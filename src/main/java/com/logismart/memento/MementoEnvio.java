package com.logismart.memento;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MementoEnvio {

    private final String estado;
    private final String origen;
    private final String destino;
    private final double peso;
    private final double costo;
    private final LocalDateTime timestamp;

    public MementoEnvio(String estado, String origen, String destino, double peso, double costo) {
        this.estado = estado;
        this.origen = origen;
        this.destino = destino;
        this.peso = peso;
        this.costo = costo;
        this.timestamp = LocalDateTime.now();
    }

    public String obtenerEstado() {
        return estado;
    }

    public String obtenerOrigen() {
        return origen;
    }

    public String obtenerDestino() {
        return destino;
    }

    public double obtenerPeso() {
        return peso;
    }

    public double obtenerCosto() {
        return costo;
    }

    public LocalDateTime obtenerTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "[" + timestamp.format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS")) + "] "
                + estado + " | " + origen + " -> " + destino
                + " | " + peso + "kg | $" + costo;
    }
}
