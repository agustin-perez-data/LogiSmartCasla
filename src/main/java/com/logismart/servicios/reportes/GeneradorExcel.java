package com.logismart.servicios.reportes;

public class GeneradorExcel implements GeneradorReporte {

    @Override
    public String formatear(String contenido) {
        StringBuilder excel = new StringBuilder();
        excel.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        excel.append("<Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\">\n");
        excel.append("  <Worksheet ss:Name=\"Reporte\">\n");
        excel.append("    <Table>\n");

        for (String linea : contenido.split("\n")) {
            if (!linea.isBlank()) {
                excel.append("      <Row><Cell><Data>")
                     .append(linea.trim())
                     .append("</Data></Cell></Row>\n");
            }
        }

        excel.append("    </Table>\n");
        excel.append("  </Worksheet>\n");
        excel.append("</Workbook>");
        return excel.toString();
    }

    @Override
    public String obtenerExtension() {
        return "xlsx";
    }
}
