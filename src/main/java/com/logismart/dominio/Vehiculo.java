package com.logismart.dominio;

public abstract class Vehiculo {
    private int id;
    private String patente;
    private double capacidad;
    private int idFlota;

    public Vehiculo(int id, String patente, double capacidad, int idFlota) {
        this.id = id;
        this.patente = patente;
        this.capacidad = capacidad;
        this.idFlota = idFlota;
    }

    public abstract boolean puedeTransportar(double peso);
    public abstract double getVelocidadPromedio();
    public abstract boolean requiereLicenciaEspecial();

    public void conducir() {
        System.out.println(getClass().getSimpleName() + " conduciendo a " +
                           getVelocidadPromedio() + " km/h | patente: " + patente);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPatente() { return patente; }
    public void setPatente(String patente) { this.patente = patente; }

    public double getCapacidad() { return capacidad; }
    public void setCapacidad(double capacidad) { this.capacidad = capacidad; }

    public int getIdFlota() { return idFlota; }
    public void setIdFlota(int idFlota) { this.idFlota = idFlota; }
}
