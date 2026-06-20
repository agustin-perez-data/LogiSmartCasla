package com.logismart.dominio;

public class Camioneta extends Vehiculo {

    public Camioneta(int id, String patente, double capacidad, int idFlota) {
        super(id, patente, capacidad, idFlota);
    }

    @Override
    public boolean puedeTransportar(double peso) {
        return peso <= getCapacidad();
    }

    @Override
    public double getVelocidadPromedio() {
        return 80.0;
    }

    @Override
    public boolean requiereLicenciaEspecial() {
        return false;
    }
}
