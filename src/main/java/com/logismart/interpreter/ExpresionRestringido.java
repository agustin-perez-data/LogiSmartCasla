package com.logismart.interpreter;

import com.logismart.dominio.Envio;

public class ExpresionRestringido implements Expresion {

    @Override
    public boolean evaluar(Envio envio) {
        return envio.getDestino().contains("Restringido");
    }
}
