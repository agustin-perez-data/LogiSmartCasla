package com.logismart.servicios;

import com.logismart.dominio.Ruta;
import com.logismart.dominio.Vehiculo;

public interface EstrategiaDeCostos {
    double calcularCosto(Ruta ruta, Vehiculo vehiculo);
}
