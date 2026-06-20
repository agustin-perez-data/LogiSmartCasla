package com.logismart.mediator;

import com.logismart.dominio.Envio;

public class ComponenteValidador {

    private final MediadorEnvios mediador;

    public ComponenteValidador(MediadorEnvios mediador) {
        this.mediador = mediador;
    }

    public void validar(Envio envio) {
        System.out.println("[Validador] Validando envio " + envio.getId() + "...");
        if (envio.getCosto() > 0 && envio.getPeso() > 0) {
            System.out.println("  OK: envio valido");
            mediador.notificar("VALIDACION_OK", envio);
            return;
        }
        System.err.println("  Error: envio invalido, flujo detenido");
    }
}
