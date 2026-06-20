package com.logismart.state;

import com.logismart.dominio.Envio;

public class StateDemo {

    public static void main(String[] args) {
        System.out.println("=== Caso 1: flujo normal ===");
        Envio envio1 = crearEnvio("ENV-001");
        envio1.validar();
        envio1.entregar();
        envio1.entregar();

        System.out.println("\n=== Caso 2: cancelacion ===");
        Envio envio2 = crearEnvio("ENV-002");
        envio2.cancelar();

        System.out.println("\n=== Caso 3: retencion ===");
        Envio envio3 = crearEnvio("ENV-003");
        envio3.validar();
        envio3.retener();
        envio3.entregar();

        System.out.println("\n=== Caso 4: devolucion ===");
        Envio envio4 = crearEnvio("ENV-004");
        envio4.validar();
        envio4.entregar();
        envio4.devolver();

        System.out.println("\n=== Caso 5: reclamo ===");
        Envio envio5 = crearEnvio("ENV-005");
        envio5.validar();
        envio5.entregar();
        envio5.entregar();
        envio5.reclamar();
    }

    private static Envio crearEnvio(String id) {
        return new Envio.EnvioBuilder(id, "Buenos Aires", "Cordoba")
                .peso(5.0)
                .tipo("NORMAL")
                .build();
    }
}
