package com.logismart.mediator;

import com.logismart.dominio.Envio;

public class SistemaNotificacion {

    private final MediadorEnvios mediador;

    public SistemaNotificacion(MediadorEnvios mediador) {
        this.mediador = mediador;
    }

    public void enviarConfirmacion(Envio envio) {
        System.out.println("[Notificador] Enviando confirmacion para envio: " + envio.getId());
        System.out.println("  OK: confirmacion enviada al cliente");
        mediador.notificar("NOTIFICACION_ENVIADA", envio);
    }
}
