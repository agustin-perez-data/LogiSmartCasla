package com.logismart.strategy;

import com.logismart.dominio.Envio;

public class EstrategiaVolumen implements EstrategiaCalculoCosto {

    @Override
    public double calcular(Envio envio) {
        double volumenEstimado = envio.getPeso() * 2.0;
        return volumenEstimado * 2.0;
    }

    @Override
    public String obtenerNombre() {
        return "Por Volumen";
    }
}
