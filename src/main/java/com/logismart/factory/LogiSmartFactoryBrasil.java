package com.logismart.factory;

import com.logismart.dominio.Moto;
import com.logismart.dominio.Vehiculo;
import com.logismart.servicios.CalculadorCostos;
import com.logismart.servicios.CalculadorCostosBrasil;
import com.logismart.servicios.HereMaps;
import com.logismart.servicios.ProveedorMapas;

public class LogiSmartFactoryBrasil implements LogiSmartFactory {

    @Override
    public Vehiculo crearVehiculo() {
        return new Moto(0, "", 20.0, 0); // Vehiculo tipico de Brasil
    }

    @Override
    public CalculadorCostos crearCalculadorCostos() {
        return new CalculadorCostosBrasil();
    }

    @Override
    public ProveedorMapas crearProveedorMapas() {
        return new HereMaps();
    }
}
