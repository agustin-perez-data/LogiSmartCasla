package com.logismart.servicios.adapters;

import com.logismart.dominio.Envio;
import com.logismart.servicios.ProveedorEnvio;
import com.logismart.servicios.externas.DHLAPI;
import com.logismart.singleton.LoggerLogiSmart;

public class AdapterDHL implements ProveedorEnvio {
    private final DHLAPI dhlAPI;
    private final LoggerLogiSmart logger = LoggerLogiSmart.obtenerInstancia();

    public AdapterDHL() {
        this.dhlAPI = new DHLAPI();
    }

    @Override
    public boolean crearEnvio(Envio envio) {
        String codigo = dhlAPI.registrarPaquete(
            envio.getOrigen(),
            envio.getDestino(),
            envio.getPeso()
        );
        envio.setNumeroSeguimiento(codigo);
        logger.info("AdapterDHL: envío creado con código " + codigo);
        return codigo != null;
    }

    @Override
    public String obtenerEstado(String numeroSeguimiento) {
        return dhlAPI.consultarEstadoPaquete(numeroSeguimiento);
    }

    @Override
    public double calcularCosto(Envio envio) {
        return dhlAPI.calcularTarifa(
            envio.getOrigen(),
            envio.getDestino(),
            envio.getPeso()
        );
    }

    @Override
    public String obtenerNombre() {
        return "DHL";
    }
}
