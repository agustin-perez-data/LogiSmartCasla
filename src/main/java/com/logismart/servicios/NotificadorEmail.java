package com.logismart.servicios;

public class NotificadorEmail implements Notificador {
    private final String smtpServer;

    public NotificadorEmail(String smtpServer) {
        this.smtpServer = smtpServer;
    }

    @Override
    public void notificar(String destinatario, String mensaje) {
        System.out.println("[EMAIL via " + smtpServer + "] Para: " + destinatario + " | " + mensaje);
    }
}
