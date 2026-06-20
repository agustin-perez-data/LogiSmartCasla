package com.logismart.singleton;

public class Logger {

    private static final Logger INSTANCIA = new Logger();
    private final LoggerLogiSmart impl = LoggerLogiSmart.obtenerInstancia();

    private Logger() {}

    public static Logger getInstance() {
        return INSTANCIA;
    }

    public void info(String mensaje)    { impl.info(mensaje); }
    public void warning(String mensaje) { impl.warning(mensaje); }
    public void error(String mensaje)   { impl.error(mensaje); }
    public void debug(String mensaje)   { impl.debug(mensaje); }
}
