package com.logismart.observer;

import com.logismart.dominio.Envio;

public interface ObservadorEnvio {
    void actualizar(Envio envio, String evento);
}
