package com.logismart.servicios.reportes;

import java.util.Map;

public class ReporteDesempenoProveedores extends Reporte {
    private final Map<String, Integer> desempenoProveedores;

    public ReporteDesempenoProveedores(GeneradorReporte generador,
                                       Map<String, Integer> desempenoProveedores) {
        super(generador);
        this.desempenoProveedores = desempenoProveedores;
    }

    @Override
    public String generarContenido() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== REPORTE DE DESEMPEÑO DE PROVEEDORES ===\n\n");

        int totalEnvios = 0;
        for (Map.Entry<String, Integer> entry : desempenoProveedores.entrySet()) {
            sb.append("Proveedor: ").append(entry.getKey()).append("\n");
            sb.append("Envíos completados: ").append(entry.getValue()).append("\n");
            totalEnvios += entry.getValue();
            sb.append("---\n");
        }

        sb.append("\nTotal general: ").append(totalEnvios).append(" envíos\n");

        return sb.toString();
    }
}
