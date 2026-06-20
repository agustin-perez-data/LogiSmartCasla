package com.logismart.interpreter;

import com.logismart.dominio.Envio;

public class ExpresionPeso implements Expresion {

    private final double valor;
    private final String operador; // "<", ">", "="

    public ExpresionPeso(double valor, String operador) {
        this.valor = valor;
        this.operador = operador;
    }

    @Override
    public boolean evaluar(Envio envio) {
        switch (operador) {
            case "<":  return envio.getPeso() < valor;
            case ">":  return envio.getPeso() > valor;
            case "=":  return envio.getPeso() == valor;
            default:   return false;
        }
    }
}
