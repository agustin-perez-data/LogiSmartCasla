package com.logismart.factory;

import com.logismart.dominio.TipoNotificador;
import com.logismart.servicios.Notificador;
import com.logismart.servicios.NotificadorEmail;
import com.logismart.servicios.NotificadorPush;
import com.logismart.servicios.NotificadorSMS;
import com.logismart.singleton.ConfiguracionLogiSmart;

public class NotificadorFactory {

    public static Notificador crearNotificador(TipoNotificador tipo) {
        ConfiguracionLogiSmart config = ConfiguracionLogiSmart.obtenerInstancia();
        return switch (tipo) {
            case EMAIL -> new NotificadorEmail(config.getSmtpServer());
            case SMS   -> new NotificadorSMS(config.getTwilioKey());
            case PUSH  -> new NotificadorPush(config.getFirebaseKey());
        };
    }
}
