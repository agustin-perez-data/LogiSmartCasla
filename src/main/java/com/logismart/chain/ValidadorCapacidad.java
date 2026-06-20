package com.logismart.chain;

import com.logismart.dominio.Envio;

public class ValidadorCapacidad extends ValidadorEnvio {

    private final SistemaCapacidad sistemaCapacidad;

    public ValidadorCapacidad(SistemaCapacidad sistemaCapacidad) {
        this.sistemaCapacidad = sistemaCapacidad;
    }

    @Override
    public boolean validar(Envio envio) {
        System.out.println("[" + obtenerNombre() + "] Verificando capacidad...");

        if (!sistemaCapacidad.hayEspacioDisponible(envio.getPeso())) {
            System.err.println("  ✗ No hay espacio disponible");
            return false;
        }

        System.out.println("  ✓ Capacidad disponible");
        return siguiente == null || siguiente.validar(envio);
    }

    @Override
    public String obtenerNombre() {
        return "ValidadorCapacidad";
    }
}
