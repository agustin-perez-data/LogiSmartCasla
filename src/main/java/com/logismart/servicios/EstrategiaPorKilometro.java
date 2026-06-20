package com.logismart.servicios;

import com.logismart.dominio.Ruta;
import com.logismart.dominio.Vehiculo;

public class EstrategiaPorKilometro implements EstrategiaDeCostos {
    private static final double TARIFA_POR_KM = 5.0;

    @Override
    public double calcularCosto(Ruta ruta, Vehiculo vehiculo) {
        double distancia = ruta.calcularDistanciaTotal();
        return distancia * TARIFA_POR_KM;
    }
}
