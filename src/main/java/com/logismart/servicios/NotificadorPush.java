package com.logismart.servicios;

public class NotificadorPush implements Notificador {
    private final String firebaseKey;

    public NotificadorPush(String firebaseKey) {
        this.firebaseKey = firebaseKey;
    }

    @Override
    public void notificar(String destinatario, String mensaje) {
        System.out.println("[PUSH via Firebase] Para: " + destinatario + " | " + mensaje);
    }
}
