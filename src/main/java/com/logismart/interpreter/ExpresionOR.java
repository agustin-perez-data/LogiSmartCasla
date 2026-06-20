package com.logismart.interpreter;

import com.logismart.dominio.Envio;

public class ExpresionOR implements Expresion {

    private final Expresion izquierda;
    private final Expresion derecha;

    public ExpresionOR(Expresion izquierda, Expresion derecha) {
        this.izquierda = izquierda;
        this.derecha = derecha;
    }

    @Override
    public boolean evaluar(Envio envio) {
        return izquierda.evaluar(envio) || derecha.evaluar(envio);
    }
}
