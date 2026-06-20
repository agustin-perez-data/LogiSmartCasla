package com.logismart.chain;

import com.logismart.dominio.Envio;

public class ValidadorInventario extends ValidadorEnvio {

    private final SistemaInventario inventario;

    public ValidadorInventario(SistemaInventario inventario) {
        this.inventario = inventario;
    }

    @Override
    public boolean validar(Envio envio) {
        System.out.println("[" + obtenerNombre() + "] Verificando stock...");

        if (!inventario.verificarStock(envio.getProductoId())) {
            System.err.println("  ✗ Stock insuficiente");
            return false;
        }

        System.out.println("  ✓ Stock disponible");
        return siguiente == null || siguiente.validar(envio);
    }

    @Override
    public String obtenerNombre() {
        return "ValidadorInventario";
    }
}
