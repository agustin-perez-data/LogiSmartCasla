package com.logismart.servicios;

public class CalculadorCostosArgentina implements CalculadorCostos {
    private static final double TARIFA_BASE_KM  = 8.0;   // ARS por km
    private static final double TARIFA_BASE_KG  = 3.0;   // ARS por kg
    private static final double IIBB             = 0.035; // 3,5% Ingresos Brutos

    @Override
    public double calcular(double distanciaKm, double pesoKg) {
        double costoBase = (distanciaKm * TARIFA_BASE_KM) + (pesoKg * TARIFA_BASE_KG);
        return costoBase * (1 + IIBB);
    }
}
