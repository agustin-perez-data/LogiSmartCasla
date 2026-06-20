package com.logismart.servicios.reportes;

import com.logismart.dominio.Envio;
import java.util.List;

public class ReporteEnvios extends Reporte {
    private final List<Envio> envios;

    public ReporteEnvios(GeneradorReporte generador, List<Envio> envios) {
        super(generador);
        this.envios = envios;
    }

    @Override
    public String generarContenido() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== REPORTE DE ENVIOS ===\n");
        sb.append("Total de envíos: ").append(envios.size()).append("\n\n");

        for (Envio envio : envios) {
            sb.append("ID: ").append(envio.getShipmentId()).append("\n");
            sb.append("Origen: ").append(envio.getOrigen()).append("\n");
            sb.append("Destino: ").append(envio.getDestino()).append("\n");
            sb.append("Estado: ").append(envio.getEstado()).append("\n");
            sb.append("Peso: ").append(envio.getPeso()).append(" kg\n");
            sb.append("---\n");
        }

        return sb.toString();
    }
}
