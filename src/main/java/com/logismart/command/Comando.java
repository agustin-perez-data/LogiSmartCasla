package com.logismart.command;

public interface Comando {
    void ejecutar();
    void deshacer();
    String obtenerDescripcion();
}
