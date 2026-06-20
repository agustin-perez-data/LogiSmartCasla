package com.logismart.singleton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerLogiSmart {

    public enum Nivel { DEBUG, INFO, WARNING, ERROR }

    private static final LoggerLogiSmart INSTANCIA = new LoggerLogiSmart();
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private LoggerLogiSmart() {}

    public static LoggerLogiSmart obtenerInstancia() {
        return INSTANCIA;
    }

    public synchronized void log(Nivel nivel, String mensaje) {
        String timestamp = LocalDateTime.now().format(FORMATO);
        System.out.println("[" + timestamp + "] [" + nivel + "] " + mensaje);
    }

    public void info(String mensaje)    { log(Nivel.INFO,    mensaje); }
    public void warning(String mensaje) { log(Nivel.WARNING, mensaje); }
    public void error(String mensaje)   { log(Nivel.ERROR,   mensaje); }
    public void debug(String mensaje)   { log(Nivel.DEBUG,   mensaje); }
}
