package com.logismart.chain;

import com.logismart.dominio.Envio;

public class ValidadorPago extends ValidadorEnvio {

    @Override
    public boolean validar(Envio envio) {
        System.out.println("[" + obtenerNombre() + "] Verificando pago...");

        if (envio.getCosto() <= 0) {
            System.err.println("  ✗ Costo inválido");
            return false;
        }
        if (envio.getMetodoPago() == null || envio.getMetodoPago().isEmpty()) {
            System.err.println("  ✗ Método de pago no especificado");
            return false;
        }

        System.out.println("  ✓ Pago válido");
        return siguiente == null || siguiente.validar(envio);
    }

    @Override
    public String obtenerNombre() {
        return "ValidadorPago";
    }
}
