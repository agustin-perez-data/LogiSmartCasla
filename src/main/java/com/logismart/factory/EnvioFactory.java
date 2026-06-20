package com.logismart.factory;

import com.logismart.dominio.Envio;
import com.logismart.dominio.EnvioEconomico;
import com.logismart.dominio.EnvioExpress;
import com.logismart.dominio.EnvioStandard;
import com.logismart.dominio.TipoEnvio;
import com.logismart.singleton.ConfiguracionLogiSmart;
import com.logismart.singleton.LoggerLogiSmart;

import java.time.LocalDate;

public class EnvioFactory {

    public static Envio crearEnvio(TipoEnvio tipo, String shipmentId,
                                   String origen, String destino,
                                   double peso, LocalDate fechaEstimadaEntrega,
                                   String ventanaInicio, String ventanaFin,
                                   String direccion, double latitud, double longitud,
                                   int destinatarioId) {

        double maxPeso = ConfiguracionLogiSmart.obtenerInstancia().getMaxPesoTotal();
        if (peso > maxPeso) {
            throw new IllegalArgumentException(
                "Peso " + peso + "kg supera el maximo permitido de " + maxPeso + "kg");
        }

        Envio.EnvioBuilder builder = new Envio.EnvioBuilder(shipmentId, origen, destino)
            .peso(peso)
            .fechaEstimadaEntrega(fechaEstimadaEntrega)
            .ventanaHoraria(ventanaInicio, ventanaFin)
            .direccionEntrega(direccion)
            .coordenadas(latitud, longitud)
            .destinatarioId(destinatarioId);

        Envio envio = switch (tipo) {
            case EXPRESS   -> new EnvioExpress(builder);
            case STANDARD  -> new EnvioStandard(builder);
            case ECONOMICO -> new EnvioEconomico(builder);
        };

        LoggerLogiSmart.obtenerInstancia().info(
            "Envio creado: " + tipo + " | id=" + shipmentId + " | peso=" + peso + "kg");
        return envio;
    }
}
