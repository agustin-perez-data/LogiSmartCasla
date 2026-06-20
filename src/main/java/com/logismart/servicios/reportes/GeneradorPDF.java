package com.logismart.servicios.reportes;

public class GeneradorPDF implements GeneradorReporte {

    @Override
    public String formatear(String contenido) {
        StringBuilder pdf = new StringBuilder();
        pdf.append("%PDF-1.4\n");
        pdf.append("% LogiSmart - Reporte Generado\n");
        pdf.append("% ================================\n\n");
        pdf.append(contenido);
        pdf.append("\n\n%%EOF");
        return pdf.toString();
    }

    @Override
    public String obtenerExtension() {
        return "pdf";
    }
}
