package com.logismart.singleton;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfiguracionLogiSmart {

    private static volatile ConfiguracionLogiSmart instancia;

    private int maxEnviosPorRuta;
    private double maxPesoTotal;
    private String dbUrl;
    private String smtpServer;
    private String twilioKey;
    private String firebaseKey;

    private ConfiguracionLogiSmart() {
        cargarPropiedades();
    }

    public static ConfiguracionLogiSmart obtenerInstancia() {
        if (instancia == null) {
            synchronized (ConfiguracionLogiSmart.class) {
                if (instancia == null) {
                    instancia = new ConfiguracionLogiSmart();
                }
            }
        }
        return instancia;
    }

    private void cargarPropiedades() {
        Properties props = new Properties();
        try (InputStream is = getClass().getClassLoader()
                .getResourceAsStream("logismart.properties")) {
            if (is != null) {
                props.load(is);
            }
        } catch (IOException e) {
            // Usa valores por defecto si el archivo no existe
        }
        maxEnviosPorRuta = Integer.parseInt(props.getProperty("max.envios.por.ruta", "20"));
        maxPesoTotal     = Double.parseDouble(props.getProperty("max.peso.total", "1000.0"));
        dbUrl            = props.getProperty("db.url",          "jdbc:h2:mem:logismart");
        smtpServer       = props.getProperty("smtp.server",     "smtp.logismart.com");
        twilioKey        = props.getProperty("twilio.key",      "TWILIO_KEY_DEFAULT");
        firebaseKey      = props.getProperty("firebase.key",    "FIREBASE_KEY_DEFAULT");
    }

    public void recargar() {
        cargarPropiedades();
    }

    public int getMaxEnviosPorRuta()  { return maxEnviosPorRuta; }
    public double getMaxPesoTotal()   { return maxPesoTotal; }
    public String getDbUrl()          { return dbUrl; }
    public String getSmtpServer()     { return smtpServer; }
    public String getTwilioKey()      { return twilioKey; }
    public String getFirebaseKey()    { return firebaseKey; }
}
