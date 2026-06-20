package com.logismart.decorator;

public class EnvioBasico implements Envio {
    private String origen;
    private String destino;
    private double peso;
    private String numeroSeguimiento;

    public EnvioBasico(String origen, String destino, double peso) {
        this.origen = origen;
        this.destino = destino;
        this.peso = peso;
        this.numeroSeguimiento = "ENV-" + System.currentTimeMillis();
    }

    @Override
    public double obtenerCosto() {
        return peso * 10.0;
    }

    @Override
    public int obtenerTiempoEntrega() {
        return 3;
    }

    @Override
    public String obtenerDescripcion() {
        return "Envío básico: " + origen + " -> " + destino + " (" + peso + " kg)";
    }

    @Override
    public String obtenerServicios() {
        return "Básico";
    }

    public String getOrigen() { return origen; }
    public String getDestino() { return destino; }
    public double getPeso() { return peso; }
    public String getNumeroSeguimiento() { return numeroSeguimiento; }
}
