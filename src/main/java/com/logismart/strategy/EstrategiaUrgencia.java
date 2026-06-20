package com.logismart.strategy;

import com.logismart.dominio.Envio;

public class EstrategiaUrgencia implements EstrategiaCalculoCosto {

    @Override
    public double calcular(Envio envio) {
        if ("URGENTE".equalsIgnoreCase(envio.getTipo())) {
            return 500.0;
        }
        if ("EXPRESS".equalsIgnoreCase(envio.getTipo())) {
            return 300.0;
        }
        return 100.0;
    }

    @Override
    public String obtenerNombre() {
        return "Por Urgencia";
    }
}
