package com.logismart.factory;

import com.logismart.dominio.Auto;
import com.logismart.dominio.Vehiculo;
import com.logismart.servicios.CalculadorCostos;
import com.logismart.servicios.CalculadorCostosArgentina;
import com.logismart.servicios.GoogleMapsArgentina;
import com.logismart.servicios.ProveedorMapas;

public class LogiSmartFactoryArgentina implements LogiSmartFactory {

    @Override
    public Vehiculo crearVehiculo() {
        return new Auto(0, "", 500.0, 0); // Vehiculo tipico de Argentina
    }

    @Override
    public CalculadorCostos crearCalculadorCostos() {
        return new CalculadorCostosArgentina();
    }

    @Override
    public ProveedorMapas crearProveedorMapas() {
        return new GoogleMapsArgentina();
    }
}
