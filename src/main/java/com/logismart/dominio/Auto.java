package com.logismart.dominio;

public class Auto extends Vehiculo {

    public Auto(int id, String patente, double capacidad, int idFlota) {
        super(id, patente, capacidad, idFlota);
    }

    @Override
    public boolean puedeTransportar(double peso) {
        return peso <= getCapacidad();
    }

    @Override
    public double getVelocidadPromedio() {
        return 100.0;
    }

    @Override
    public boolean requiereLicenciaEspecial() {
        return false;
    }
}
