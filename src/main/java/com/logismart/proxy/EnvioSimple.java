package com.logismart.proxy;

public class EnvioSimple {
    private String id;
    private String origen;
    private String destino;
    private double peso;
    private String estado;

    public EnvioSimple(String id, String origen, String destino, double peso, String estado) {
        this.id = id;
        this.origen = origen;
        this.destino = destino;
        this.peso = peso;
        this.estado = estado;
    }

    public String getId() { return id; }
    public String getOrigen() { return origen; }
    public String getDestino() { return destino; }
    public double getPeso() { return peso; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "Envio{" + id + ", " + origen + " -> " + destino + ", " + peso + "kg, " + estado + "}";
    }
}
