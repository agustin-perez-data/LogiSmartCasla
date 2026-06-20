package com.logismart.strategy;

import com.logismart.dominio.Envio;

public class EstrategiaPeso implements EstrategiaCalculoCosto {

    private static final double COSTO_POR_KG = 5.0;

    @Override
    public double calcular(Envio envio) {
        return envio.getPeso() * COSTO_POR_KG;
    }

    @Override
    public String obtenerNombre() {
        return "Por Peso";
    }
}
