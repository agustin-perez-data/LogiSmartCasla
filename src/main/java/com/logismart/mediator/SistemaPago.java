package com.logismart.mediator;

import com.logismart.dominio.Envio;

public class SistemaPago {

    private final MediadorEnvios mediador;

    public SistemaPago(MediadorEnvios mediador) {
        this.mediador = mediador;
    }

    public void procesarPago(Envio envio) {
        System.out.println("[Pago] Procesando pago de $" + envio.getCosto()
                + " con metodo: " + envio.getMetodoPago());
        System.out.println("  OK: pago confirmado");
        mediador.notificar("PAGO_CONFIRMADO", envio);
    }
}
