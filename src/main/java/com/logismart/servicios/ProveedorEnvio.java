package com.logismart.servicios;

import com.logismart.dominio.Envio;

public interface ProveedorEnvio {
    boolean crearEnvio(Envio envio);
    String obtenerEstado(String numeroSeguimiento);
    double calcularCosto(Envio envio);
    String obtenerNombre();
}
