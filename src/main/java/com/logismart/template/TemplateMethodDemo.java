package com.logismart.template;

import com.logismart.dominio.Envio;

import java.util.Arrays;
import java.util.List;

public class TemplateMethodDemo {

    public static void main(String[] args) {
        Envio envio1 = crearEnvio("ENV-001", "NORMAL", 5.0);
        Envio envio2 = crearEnvio("ENV-002", "INTERNACIONAL", 8.0);
        Envio envio3 = crearEnvio("ENV-003", "URGENTE", 2.0);

        System.out.println("=== Caso 1: proceso nacional ===");
        ProcesoProcesosEnvio nacional = new ProcesoNacional();
        nacional.procesarEnvio(envio1);

        System.out.println("=== Caso 2: proceso internacional ===");
        ProcesoProcesosEnvio internacional = new ProcesoInternacional();
        internacional.procesarEnvio(envio2);

        System.out.println("=== Caso 3: proceso urgente ===");
        ProcesoProcesosEnvio urgente = new ProcesoUrgente();
        urgente.procesarEnvio(envio3);

        System.out.println("=== Caso 4: multiples procesos ===");
        List<Envio> envios = Arrays.asList(envio1, envio2, envio3);
        List<ProcesoProcesosEnvio> procesos = Arrays.asList(nacional, internacional, urgente);
        for (int i = 0; i < envios.size(); i++) {
            procesos.get(i).procesarEnvio(envios.get(i));
        }

        System.out.println("=== Caso 5: extensibilidad ===");
        System.out.println("Se puede agregar ProcesoExpress extendiendo ProcesoProcesosEnvio.");
    }

    private static Envio crearEnvio(String id, String tipo, double peso) {
        return new Envio.EnvioBuilder(id, "Buenos Aires", "Cordoba")
                .peso(peso)
                .tipo(tipo)
                .build();
    }
}
