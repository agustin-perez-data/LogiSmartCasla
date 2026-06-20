package com.logismart.chain;

import com.logismart.dominio.Envio;

public class CadenaValidadores {

    private final ValidadorEnvio primerValidador;

    public CadenaValidadores(SistemaInventario inventario, SistemaCapacidad capacidad) {
        ValidadorEnvio validador1 = new ValidadorDatos();
        ValidadorEnvio validador2 = new ValidadorInventario(inventario);
        ValidadorEnvio validador3 = new ValidadorPago();
        ValidadorEnvio validador4 = new ValidadorSeguridad();
        ValidadorEnvio validador5 = new ValidadorCapacidad(capacidad);

        validador1.setSiguiente(validador2);
        validador2.setSiguiente(validador3);
        validador3.setSiguiente(validador4);
        validador4.setSiguiente(validador5);

        this.primerValidador = validador1;
    }

    public boolean validarEnvio(Envio envio) {
        System.out.println("\n=== Validando Envío ===");
        return primerValidador.validar(envio);
    }
}
