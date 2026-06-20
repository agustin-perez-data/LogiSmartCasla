package com.logismart.strategy;

import com.logismart.dominio.Envio;

public class EstrategiaDistancia implements EstrategiaCalculoCosto {

    private static final double COSTO_POR_KM = 10.0;

    @Override
    public double calcular(Envio envio) {
        return calcularDistancia(envio.getOrigen(), envio.getDestino()) * COSTO_POR_KM;
    }

    @Override
    public String obtenerNombre() {
        return "Por Distancia";
    }

    private double calcularDistancia(String origen, String destino) {
        int hash = Math.floorMod((origen + "->" + destino).hashCode(), 450);
        return 50.0 + hash;
    }
}
