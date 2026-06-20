package com.logismart.facade.subsistemas;

public class SistemaRastreo {
    public String crearNumeroSeguimiento() {
        String numero = "LS-" + System.currentTimeMillis();
        System.out.println("  [Rastreo] Número de seguimiento creado: " + numero);
        return numero;
    }

    public String obtenerEstado(String numeroSeguimiento) {
        System.out.println("  [Rastreo] Consultando estado de: " + numeroSeguimiento);
        return "EN TRÁNSITO";
    }

    public void cancelarRastreo(String numeroSeguimiento) {
        System.out.println("  [Rastreo] Rastreo cancelado para: " + numeroSeguimiento);
    }
}
