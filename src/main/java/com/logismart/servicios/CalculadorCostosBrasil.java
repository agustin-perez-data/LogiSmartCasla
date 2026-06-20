package com.logismart.servicios;

public class CalculadorCostosBrasil implements CalculadorCostos {
    private static final double TARIFA_BASE_KM  = 2.5;  // BRL por km
    private static final double TARIFA_BASE_KG  = 1.2;  // BRL por kg
    private static final double ICMS             = 0.12; // 12% ICMS

    @Override
    public double calcular(double distanciaKm, double pesoKg) {
        double costoBase = (distanciaKm * TARIFA_BASE_KM) + (pesoKg * TARIFA_BASE_KG);
        return costoBase * (1 + ICMS);
    }
}
