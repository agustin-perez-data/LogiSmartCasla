package com.logismart.interpreter;

import com.logismart.dominio.Envio;

public class ExpresionCosto implements Expresion {

    private final double valor;
    private final String operador; // "<", ">", "="

    public ExpresionCosto(double valor, String operador) {
        this.valor = valor;
        this.operador = operador;
    }

    @Override
    public boolean evaluar(Envio envio) {
        switch (operador) {
            case "<":  return envio.getCosto() < valor;
            case ">":  return envio.getCosto() > valor;
            case "=":  return envio.getCosto() == valor;
            default:   return false;
        }
    }
}
