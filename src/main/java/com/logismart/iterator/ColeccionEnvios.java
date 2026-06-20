package com.logismart.iterator;

import com.logismart.dominio.Envio;

public interface ColeccionEnvios {
    IteradorEnvios crearIterador();
    void agregar(Envio envio);
    void remover(Envio envio);
    int obtenerTamano();

    default int obtenerTamaño() {
        return obtenerTamano();
    }
}
