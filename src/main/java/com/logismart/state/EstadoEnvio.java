package com.logismart.state;

import com.logismart.dominio.Envio;

public interface EstadoEnvio {
    void validar(Envio envio);
    void entregar(Envio envio);
    void cancelar(Envio envio);
    void retener(Envio envio);
    void devolver(Envio envio);
    void reclamar(Envio envio);
    String obtenerNombre();
}
