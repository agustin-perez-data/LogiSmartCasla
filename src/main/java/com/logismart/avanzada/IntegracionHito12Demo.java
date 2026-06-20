package com.logismart.avanzada;

import com.logismart.dominio.Envio;
import com.logismart.strategy.EstrategiaHibrida;
import com.logismart.strategy.EstrategiaPeso;
import com.logismart.template.ProcesoInternacional;
import com.logismart.template.ProcesoNacional;
import com.logismart.template.ProcesoUrgente;
import com.logismart.visitor.CentroRegional;
import com.logismart.visitor.VisitorBusquedaPuntosCriticos;
import com.logismart.visitor.VisitorDemo;

import java.util.Arrays;
import java.util.List;

public class IntegracionHito12Demo {

    public static void main(String[] args) {
        SistemaLogisticaAvanzada sistema = new SistemaLogisticaAvanzada();
        CentroRegional estructura = VisitorDemo.crearEstructura();

        System.out.println("=== Caso 1: flujo completo urgente ===");
        Envio urgente = crearEnvio("ENV-I-001", "URGENTE", 4.5);
        sistema.procesarEnvioCompleto(urgente, new ProcesoUrgente(), estructura);

        System.out.println("\n=== Caso 2: flujo completo nacional ===");
        Envio nacional = crearEnvio("ENV-I-002", "NORMAL", 7.0);
        sistema.procesarEnvioCompleto(nacional, new ProcesoNacional(), estructura);

        System.out.println("\n=== Caso 3: cambio dinamico de estrategia ===");
        Envio estrategico = crearEnvio("ENV-I-003", "EXPRESS", 12.0);
        estrategico.establecerEstrategia(new EstrategiaPeso());
        System.out.println("Costo por peso: $" + String.format("%.2f", estrategico.calcularCosto()));
        estrategico.establecerEstrategia(new EstrategiaHibrida());
        System.out.println("Costo hibrido: $" + String.format("%.2f", estrategico.calcularCosto()));

        System.out.println("\n=== Caso 4: varios procesos Template Method ===");
        List<Envio> envios = Arrays.asList(
                crearEnvio("ENV-I-004", "NORMAL", 3.0),
                crearEnvio("ENV-I-005", "INTERNACIONAL", 9.0),
                crearEnvio("ENV-I-006", "URGENTE", 1.0));
        new ProcesoNacional().procesarEnvio(envios.get(0));
        new ProcesoInternacional().procesarEnvio(envios.get(1));
        new ProcesoUrgente().procesarEnvio(envios.get(2));

        System.out.println("\n=== Caso 5: analisis Visitor de puntos criticos ===");
        VisitorBusquedaPuntosCriticos criticos = new VisitorBusquedaPuntosCriticos();
        estructura.aceptar(criticos);
        System.out.println("Puntos criticos: " + criticos.obtenerPuntosCriticos());
    }

    private static Envio crearEnvio(String id, String tipo, double peso) {
        return new Envio.EnvioBuilder(id, "Buenos Aires", "Cordoba")
                .peso(peso)
                .tipo(tipo)
                .metodoPago("TARJETA")
                .productoId("PROD-" + id)
                .build();
    }
}
