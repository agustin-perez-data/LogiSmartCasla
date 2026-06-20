package com.logismart.factory;

import com.logismart.dominio.Vehiculo;
import com.logismart.servicios.CalculadorCostos;
import com.logismart.servicios.ProveedorMapas;

public interface LogiSmartFactory {
    Vehiculo crearVehiculo();
    CalculadorCostos crearCalculadorCostos();
    ProveedorMapas crearProveedorMapas();
}
