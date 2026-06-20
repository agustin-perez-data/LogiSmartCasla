package com.logismart.singleton;

public class ConfiguracionSistema {

    private static final ConfiguracionSistema INSTANCIA = new ConfiguracionSistema();
    private final ConfiguracionLogiSmart impl = ConfiguracionLogiSmart.obtenerInstancia();

    private ConfiguracionSistema() {}

    public static ConfiguracionSistema getInstance() {
        return INSTANCIA;
    }

    public int getMaxEnviosPorRuta() { return impl.getMaxEnviosPorRuta(); }
    public double getMaxPesoTotal()  { return impl.getMaxPesoTotal(); }
    public String getDbUrl()         { return impl.getDbUrl(); }
    public String getSmtpServer()    { return impl.getSmtpServer(); }
    public String getTwilioKey()     { return impl.getTwilioKey(); }
    public String getFirebaseKey()   { return impl.getFirebaseKey(); }
}
