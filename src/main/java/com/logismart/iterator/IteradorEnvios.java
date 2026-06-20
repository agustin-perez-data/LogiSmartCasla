package com.logismart.iterator;

import com.logismart.dominio.Envio;

public interface IteradorEnvios {
    boolean tieneSiguiente();
    Envio obtenerSiguiente();
    void reiniciar();
}
