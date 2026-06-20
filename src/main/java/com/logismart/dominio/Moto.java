package com.logismart.dominio;

public class Moto extends Vehiculo {
    private static final double LIMITE_PESO_PAQUETE = 20.0;

    public Moto(int id, String patente, double capacidad, int idFlota) {
        super(id, patente, capacidad, idFlota);
    }

    @Override
    public boolean puedeTransportar(double peso) {
        return peso <= getCapacidad() && peso <= LIMITE_PESO_PAQUETE;
    }

    @Override
    public double getVelocidadPromedio() {
        return 60.0;
    }

    @Override
    public boolean requiereLicenciaEspecial() {
        return false;
    }
}
