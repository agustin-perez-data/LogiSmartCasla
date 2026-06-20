package com.logismart.facade.subsistemas;

public class SistemaReportes {
    public void generarReporte(String tipo, String referencia, String formato) {
        System.out.println("  [Reportes] Generando reporte " + tipo + " en formato " + formato + " para: " + referencia);
    }
}
