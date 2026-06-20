package com.logismart.datos.dominio;

import java.time.LocalDateTime;

public class Envio {
    private int id;
    private String origen;
    private String destino;
    private double peso;
    private LocalDateTime fechaCreacion;
    private EstadoEnvio estado;

    public Envio(int id, String origen, String destino, double peso) {
        this.id = id;
        this.origen = origen;
        this.destino = destino;
        this.peso = peso;
        this.fechaCreacion = LocalDateTime.now();
        this.estado = EstadoEnvio.CONFIRMADO;
    }

    public int getId() { return id; }
    public String getOrigen() { return origen; }
    public String getDestino() { return destino; }
    public double getPeso() { return peso; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public EstadoEnvio getEstado() { return estado; }
    public void setEstado(EstadoEnvio estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "Envio[" + id + "] " + origen + " -> " + destino + " (" + peso + "kg, " + estado + ")";
    }
}
