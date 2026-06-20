package com.logismart.avanzada;

import com.logismart.dominio.Envio;
import com.logismart.strategy.EstrategiaHibrida;
import com.logismart.template.ProcesoProcesosEnvio;
import com.logismart.template.ProcesoUrgente;
import com.logismart.visitor.CentroDistribucion;
import com.logismart.visitor.CentroRegional;
import com.logismart.visitor.VisitorCalculoOcupacion;
import com.logismart.visitor.VisitorDemo;

public class SistemaLogisticaAvanzada {

    public void procesarEnvioCompleto(Envio envio, ProcesoProcesosEnvio proceso,
                                      CentroDistribucion estructura) {
        System.out.println("=== Sistema Logistica Avanzada ===");

        System.out.println("\n[Integracion] State");
        envio.validar();
        envio.entregar();

        System.out.println("\n[Integracion] Strategy");
        envio.establecerEstrategia(new EstrategiaHibrida());
        double costo = envio.calcularCosto();
        envio.setCosto(costo);
        System.out.println("Costo calculado: $" + String.format("%.2f", costo));

        System.out.println("\n[Integracion] Template Method");
        proceso.procesarEnvio(envio);

        System.out.println("[Integracion] Visitor");
        VisitorCalculoOcupacion visitor = new VisitorCalculoOcupacion();
        estructura.aceptar(visitor);
        System.out.println("Ocupacion promedio: "
                + String.format("%.2f", visitor.obtenerOcupacionPromedio()) + "%");
    }

    public static void main(String[] args) {
        Envio envio = new Envio.EnvioBuilder("ENV-AV-001", "Buenos Aires", "Cordoba")
                .peso(4.5)
                .tipo("URGENTE")
                .metodoPago("TARJETA")
                .productoId("PROD-AV-001")
                .build();

        CentroRegional estructura = VisitorDemo.crearEstructura();
        new SistemaLogisticaAvanzada().procesarEnvioCompleto(
                envio, new ProcesoUrgente(), estructura);
    }
}
