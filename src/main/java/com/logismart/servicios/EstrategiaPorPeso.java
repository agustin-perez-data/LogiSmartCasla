package com.logismart.servicios;

import com.logismart.dominio.Envio;
import com.logismart.dominio.Ruta;
import com.logismart.dominio.Vehiculo;

public class EstrategiaPorPeso implements EstrategiaDeCostos {
    private static final double TARIFA_POR_KG = 2.5;

    @Override
    public double calcularCosto(Ruta ruta, Vehiculo vehiculo) {
        double pesoTotal = ruta.getEnvios().stream()
            .mapToDouble(Envio::getPeso)
            .sum();
        return pesoTotal * TARIFA_POR_KG;
    }
}
