package com.logismart.chain;

import com.logismart.dominio.Envio;

public class ValidadorSeguridad extends ValidadorEnvio {

    @Override
    public boolean validar(Envio envio) {
        System.out.println("[" + obtenerNombre() + "] Verificando restricciones...");

        if (envio.getDestino().contains("Restringido")) {
            System.err.println("  ✗ Destino restringido");
            return false;
        }

        System.out.println("  ✓ Seguridad OK");
        return siguiente == null || siguiente.validar(envio);
    }

    @Override
    public String obtenerNombre() {
        return "ValidadorSeguridad";
    }
}
