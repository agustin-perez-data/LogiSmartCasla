package com.logismart.servicios.externas;

public class DHLAPI {
    public String registrarPaquete(String origen, String destino, double peso) {
        System.out.println("[DHL API] Registrando paquete: " + origen + " → " + destino);
        return "DHL-" + System.currentTimeMillis();
    }

    public String consultarEstadoPaquete(String codigo) {
        return "En tránsito";
    }

    public double calcularTarifa(String origen, String destino, double peso) {
        return peso * 15.0;
    }
}
