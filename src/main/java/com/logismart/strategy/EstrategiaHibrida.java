package com.logismart.strategy;

import com.logismart.dominio.Envio;

public class EstrategiaHibrida implements EstrategiaCalculoCosto {

    @Override
    public double calcular(Envio envio) {
        double costoDistancia = new EstrategiaDistancia().calcular(envio);
        double costoPeso = new EstrategiaPeso().calcular(envio);
        double costoUrgencia = new EstrategiaUrgencia().calcular(envio);
        return (costoDistancia * 0.4) + (costoPeso * 0.3) + (costoUrgencia * 0.3);
    }

    @Override
    public String obtenerNombre() {
        return "Hibrida";
    }
}
