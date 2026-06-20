package com.logismart.facade.subsistemas;

public class SistemaNotificaciones {
    public void enviarEmail(String email, String mensaje) {
        System.out.println("  [Notificaciones] Email a " + email + ": " + mensaje);
    }

    public void enviarSMS(String telefono, String mensaje) {
        System.out.println("  [Notificaciones] SMS a " + telefono + ": " + mensaje);
    }
}
