package com.logismart.servicios.reportes;

public class GeneradorJSON implements GeneradorReporte {

    @Override
    public String formatear(String contenido) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"sistema\": \"LogiSmart\",\n");
        json.append("  \"reporte\": \"")
            .append(contenido.replace("\n", "\\n").replace("\"", "\\\""))
            .append("\"\n");
        json.append("}");
        return json.toString();
    }

    @Override
    public String obtenerExtension() {
        return "json";
    }
}
