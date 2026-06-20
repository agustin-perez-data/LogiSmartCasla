package com.logismart.servicios.reportes;

public class GeneradorCSV implements GeneradorReporte {

    @Override
    public String formatear(String contenido) {
        StringBuilder csv = new StringBuilder();
        for (String linea : contenido.split("\n")) {
            if (!linea.isBlank() && !linea.startsWith("===")) {
                String lineaCSV = linea.replace(": ", ",").replace("---", "");
                if (!lineaCSV.isBlank()) {
                    csv.append(lineaCSV).append("\r\n");
                }
            }
        }
        return csv.toString();
    }

    @Override
    public String obtenerExtension() {
        return "csv";
    }
}
