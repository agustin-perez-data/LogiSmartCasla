package com.logismart.factory;

import com.logismart.dominio.Camion;
import com.logismart.dominio.Camioneta;
import com.logismart.dominio.Moto;
import com.logismart.dominio.TipoVehiculo;
import com.logismart.dominio.Vehiculo;
import com.logismart.singleton.LoggerLogiSmart;

public class VehiculoFactory {

    public static Vehiculo crearVehiculo(TipoVehiculo tipo, String patente, double capacidad, int idFlota) {
        LoggerLogiSmart logger = LoggerLogiSmart.obtenerInstancia();
        Vehiculo vehiculo = switch (tipo) {
            case MOTO      -> new Moto(0, patente, capacidad, idFlota);
            case CAMIONETA -> new Camioneta(0, patente, capacidad, idFlota);
            case CAMION    -> new Camion(0, patente, capacidad, idFlota);
        };
        logger.info("Vehiculo creado: " + tipo + " | patente=" + patente + " | capacidad=" + capacidad + "kg");
        return vehiculo;
    }
}
