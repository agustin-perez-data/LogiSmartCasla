package com.logismart.servicios.adapters;

import com.logismart.dominio.Envio;
import com.logismart.servicios.ProveedorEnvio;
import com.logismart.servicios.externas.FedExAPI;
import com.logismart.singleton.LoggerLogiSmart;

public class AdapterFedEx implements ProveedorEnvio {
    private final FedExAPI fedexAPI;
    private final LoggerLogiSmart logger = LoggerLogiSmart.obtenerInstancia();

    public AdapterFedEx() {
        this.fedexAPI = new FedExAPI();
    }

    @Override
    public boolean crearEnvio(Envio envio) {
        int shipmentId = fedexAPI.crearShipment(
            envio.getOrigen(),
            envio.getDestino(),
            envio.getPeso()
        );
        envio.setNumeroSeguimiento("FDX-" + shipmentId);
        logger.info("AdapterFedEx: envío creado con ID " + shipmentId);
        return shipmentId > 0;
    }

    @Override
    public String obtenerEstado(String numeroSeguimiento) {
        int shipmentId = Integer.parseInt(numeroSeguimiento.replace("FDX-", ""));
        String estadoFedEx = fedexAPI.getShipmentStatus(shipmentId);
        return switch (estadoFedEx) {
            case "DELIVERED"  -> "Entregado";
            case "IN_TRANSIT" -> "En tránsito";
            case "PENDING"    -> "Pendiente";
            default           -> estadoFedEx;
        };
    }

    @Override
    public double calcularCosto(Envio envio) {
        return fedexAPI.getShippingRate(
            envio.getOrigen(),
            envio.getDestino(),
            envio.getPeso()
        );
    }

    @Override
    public String obtenerNombre() {
        return "FedEx";
    }
}
