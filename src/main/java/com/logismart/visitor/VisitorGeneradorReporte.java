package com.logismart.visitor;

public class VisitorGeneradorReporte implements VisitorCentro {

    private final StringBuilder reporte = new StringBuilder();
    private int nivel = 0;

    @Override
    public void visitar(PuntoEntrega punto) {
        agregarIndentacion();
        reporte.append("- ")
                .append(punto.obtenerNombre())
                .append(" (")
                .append(String.format("%.1f", punto.obtenerOcupacion()))
                .append("%)\n");
    }

    @Override
    public void visitar(CentroRegional centro) {
        agregarIndentacion();
        reporte.append("+ ").append(centro.obtenerNombre()).append("\n");
    }

    @Override
    public void entrarCentro(CentroRegional centro) {
        nivel++;
    }

    @Override
    public void salirCentro(CentroRegional centro) {
        nivel--;
    }

    private void agregarIndentacion() {
        reporte.append("  ".repeat(Math.max(0, nivel)));
    }

    public String obtenerReporte() {
        return reporte.toString();
    }
}
