package com.logismart.observer;

import com.logismart.dominio.Envio;

public class SistemaNotificacionObservador implements ObservadorEnvio {

    @Override
    public void actualizar(Envio envio, String evento) {
        System.out.println("[NotificacionObs] Alerta cliente | Envio "
                + envio.getId() + ": " + evento);

        switch (evento) {
            case "EN_PREPARACION" -> System.out.println("  SMS: su envio esta siendo preparado");
            case "EN_TRANSITO" -> System.out.println("  SMS: su envio esta en camino");
            case "ENTREGADO" -> System.out.println("  SMS: su envio fue entregado");
            case "DEVUELTO" -> System.out.println("  SMS: su envio fue devuelto");
            default -> System.out.println("  Email: estado actualizado a " + evento);
        }
    }
}
