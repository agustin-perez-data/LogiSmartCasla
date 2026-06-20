package com.logismart.servicios;

public class NotificadorSMS implements Notificador {
    private final String twilioKey;

    public NotificadorSMS(String twilioKey) {
        this.twilioKey = twilioKey;
    }

    @Override
    public void notificar(String destinatario, String mensaje) {
        System.out.println("[SMS via Twilio] Para: " + destinatario + " | " + mensaje);
    }
}
