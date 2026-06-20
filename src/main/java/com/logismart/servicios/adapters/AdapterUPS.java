package com.logismart.servicios.adapters;

import com.logismart.dominio.Envio;
import com.logismart.servicios.ProveedorEnvio;
import com.logismart.servicios.externas.UPSConnector;
import com.logismart.singleton.LoggerLogiSmart;

public class AdapterUPS implements ProveedorEnvio {
    private final UPSConnector upsConnector;
    private final LoggerLogiSmart logger = LoggerLogiSmart.obtenerInstancia();

    public AdapterUPS() {
        this.upsConnector = new UPSConnector();
    }

    @Override
    public boolean crearEnvio(Envio envio) {
        boolean resultado = upsConnector.sendPackage(
            envio.getOrigen(),
            envio.getDestino(),
            envio.getPeso()
        );
        if (resultado) {
            String codigo = "UPS-" + System.currentTimeMillis();
            envio.setNumeroSeguimiento(codigo);
            logger.info("AdapterUPS: envío creado con código " + codigo);
        } else {
            logger.error("AdapterUPS: falló la creación del envío");
        }
        return resultado;
    }

    @Override
    public String obtenerEstado(String numeroSeguimiento) {
        String estadoUPS = upsConnector.trackPackage(numeroSeguimiento);
        return switch (estadoUPS) {
            case "Out for delivery" -> "En camino";
            case "Delivered"        -> "Entregado";
            case "In transit"       -> "En tránsito";
            default                 -> estadoUPS;
        };
    }

    @Override
    public double calcularCosto(Envio envio) {
        return upsConnector.estimateCost(
            envio.getOrigen(),
            envio.getDestino(),
            envio.getPeso()
        );
    }

    @Override
    public String obtenerNombre() {
        return "UPS";
    }
}
