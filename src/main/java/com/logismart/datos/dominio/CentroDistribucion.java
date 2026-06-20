package com.logismart.datos.dominio;

public class CentroDistribucion {
    private int id;
    private String nombre;
    private String ciudad;
    private double capacidad;
    private double ocupacion;

    public CentroDistribucion(int id, String nombre, String ciudad, double capacidad) {
        this.id = id;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.capacidad = capacidad;
        this.ocupacion = 0;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCiudad() { return ciudad; }
    public double getCapacidad() { return capacidad; }
    public double getOcupacion() { return ocupacion; }
    public void setOcupacion(double ocupacion) { this.ocupacion = ocupacion; }

    @Override
    public String toString() {
        return "Centro[" + id + "] " + nombre + " (" + ciudad + ", " + ocupacion + "/" + capacidad + ")";
    }
}
