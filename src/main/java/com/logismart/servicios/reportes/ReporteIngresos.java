package com.logismart.servicios.reportes;

import com.logismart.dominio.Envio;
import java.util.List;

public class ReporteIngresos extends Reporte {
    private final List<Envio> envios;

    public ReporteIngresos(GeneradorReporte generador, List<Envio> envios) {
        super(generador);
        this.envios = envios;
    }

    @Override
    public String generarContenido() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== REPORTE DE INGRESOS ===\n");

        double totalIngresos = 0;
        int entregados = 0;
        int pendientes = 0;

        for (Envio envio : envios) {
            double costoEnvio = envio.getPeso() * 10.0;
            totalIngresos += costoEnvio;

            if (envio.getEstado().name().equals("ENTREGADO")) {
                entregados++;
            } else {
                pendientes++;
            }
        }

        sb.append("Total de envíos: ").append(envios.size()).append("\n");
        sb.append("Entregados: ").append(entregados).append("\n");
        sb.append("Pendientes: ").append(pendientes).append("\n");
        sb.append("Ingresos totales: $").append(String.format("%.2f", totalIngresos)).append("\n");

        if (!envios.isEmpty()) {
            sb.append("Promedio por envío: $")
              .append(String.format("%.2f", totalIngresos / envios.size())).append("\n");
        }

        return sb.toString();
    }
}
