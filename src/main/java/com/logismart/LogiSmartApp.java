package com.logismart;

import com.logismart.dominio.Vehiculo;
import com.logismart.factory.LogiSmartFactory;
import com.logismart.factory.LogiSmartFactoryArgentina;
import com.logismart.factory.LogiSmartFactoryBrasil;
import com.logismart.servicios.CalculadorCostos;
import com.logismart.servicios.ProveedorMapas;
import com.logismart.singleton.LoggerLogiSmart;

public class LogiSmartApp {
    private LogiSmartFactory factory;
    private Vehiculo vehiculo;
    private CalculadorCostos calculador;
    private ProveedorMapas mapas;
    private LoggerLogiSmart logger;

    public LogiSmartApp(String region) {
        this.logger = LoggerLogiSmart.obtenerInstancia();

        if (region.equalsIgnoreCase("Argentina")) {
            this.factory = new LogiSmartFactoryArgentina();
        } else if (region.equalsIgnoreCase("Brasil")) {
            this.factory = new LogiSmartFactoryBrasil();
        } else {
            throw new IllegalArgumentException("Region no soportada: " + region);
        }

        this.vehiculo   = factory.crearVehiculo();
        this.calculador = factory.crearCalculadorCostos();
        this.mapas      = factory.crearProveedorMapas();

        logger.info("LogiSmartApp inicializado para: " + region +
                    " | proveedor mapas: " + mapas.getNombreProveedor());
    }

    public void procesarEnvio(String origen, String destino, double peso) {
        logger.info("Procesando envio de " + origen + " a " + destino);

        vehiculo.conducir();
        double costo = calculador.calcular(100.0, peso); // distancia estimada 100 km
        mapas.obtenerRuta(origen, destino);

        logger.info("Costo calculado: $" + String.format("%.2f", costo));
    }

    public static void main(String[] args) {
        LogiSmartApp appAR = new LogiSmartApp("Argentina");
        appAR.procesarEnvio("Buenos Aires", "Córdoba", 15.0);

        System.out.println();

        LogiSmartApp appBR = new LogiSmartApp("Brasil");
        appBR.procesarEnvio("São Paulo", "Rio de Janeiro", 8.0);
    }
}
