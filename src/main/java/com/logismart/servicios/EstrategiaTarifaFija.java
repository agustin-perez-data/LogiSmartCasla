package com.logismart.servicios;

import com.logismart.dominio.Ruta;
import com.logismart.dominio.Vehiculo;

public class EstrategiaTarifaFija implements EstrategiaDeCostos {
    private static final double TARIFA_FIJA = 500.0;

    @Override
    public double calcularCosto(Ruta ruta, Vehiculo vehiculo) {
        return TARIFA_FIJA;
    }
}
