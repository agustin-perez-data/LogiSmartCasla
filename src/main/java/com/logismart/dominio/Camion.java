package com.logismart.dominio;

public class Camion extends Vehiculo {

    public Camion(int id, String patente, double capacidad, int idFlota) {
        super(id, patente, capacidad, idFlota);
    }

    @Override
    public boolean puedeTransportar(double peso) {
        return peso <= getCapacidad();
    }

    @Override
    public double getVelocidadPromedio() {
        return 90.0;
    }

    @Override
    public boolean requiereLicenciaEspecial() {
        return true;
    }
}
