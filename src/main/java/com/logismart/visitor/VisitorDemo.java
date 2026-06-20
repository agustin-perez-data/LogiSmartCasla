package com.logismart.visitor;

public class VisitorDemo {

    public static void main(String[] args) {
        CentroRegional centroNacional = crearEstructura();

        System.out.println("=== Caso 1: calcular ocupacion ===");
        VisitorCalculoOcupacion ocupacion = new VisitorCalculoOcupacion();
        centroNacional.aceptar(ocupacion);
        System.out.println("Ocupacion promedio: "
                + String.format("%.2f", ocupacion.obtenerOcupacionPromedio()) + "%\n");

        System.out.println("=== Caso 2: generar reporte ===");
        VisitorGeneradorReporte reporte = new VisitorGeneradorReporte();
        centroNacional.aceptar(reporte);
        System.out.println(reporte.obtenerReporte());

        System.out.println("=== Caso 3: calcular costo operativo ===");
        VisitorCalculoCostoOperativo costo = new VisitorCalculoCostoOperativo();
        centroNacional.aceptar(costo);
        System.out.println("Costo operativo total: $" + String.format("%.2f", costo.obtenerCostoTotal()) + "\n");

        System.out.println("=== Caso 4: buscar puntos criticos ===");
        VisitorBusquedaPuntosCriticos criticos = new VisitorBusquedaPuntosCriticos();
        centroNacional.aceptar(criticos);
        System.out.println("Puntos criticos: " + criticos.obtenerPuntosCriticos());

        System.out.println("\n=== Caso 5: multiples visitors ===");
        centroNacional.aceptar(new VisitorCalculoOcupacion());
        centroNacional.aceptar(new VisitorCalculoCostoOperativo());
        centroNacional.aceptar(new VisitorBusquedaPuntosCriticos());
    }

    public static CentroRegional crearEstructura() {
        CentroRegional centroNacional = new CentroRegional("Centro Nacional");
        CentroRegional centroCaba = new CentroRegional("Centro CABA");
        centroCaba.agregarSubcentro(new PuntoEntrega("Punto San Telmo", 75.0));
        centroCaba.agregarSubcentro(new PuntoEntrega("Punto Recoleta", 85.0));

        CentroRegional centroMendoza = new CentroRegional("Centro Mendoza");
        centroMendoza.agregarSubcentro(new PuntoEntrega("Punto Mendoza Centro", 60.0));
        centroMendoza.agregarSubcentro(new PuntoEntrega("Punto Godoy Cruz", 92.0));

        centroNacional.agregarSubcentro(centroCaba);
        centroNacional.agregarSubcentro(centroMendoza);
        return centroNacional;
    }
}
