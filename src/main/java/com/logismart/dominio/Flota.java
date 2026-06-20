package com.logismart.dominio;

import java.util.ArrayList;
import java.util.List;

public class Flota {
    private int id;
    private String nombre;
    private String descripcion;
    private int pymeId;
    private List<Vehiculo> vehiculos;

    public Flota(int id, String nombre, String descripcion, int pymeId) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.pymeId = pymeId;
        this.vehiculos = new ArrayList<>();
    }

    public void agregarVehiculo(Vehiculo vehiculo) {
        vehiculos.add(vehiculo);
    }

    public void removerVehiculo(int vehiculoId) {
        vehiculos.removeIf(v -> v.getId() == vehiculoId);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getPymeId() { return pymeId; }
    public void setPymeId(int pymeId) { this.pymeId = pymeId; }

    public List<Vehiculo> getVehiculos() { return vehiculos; }
    public void setVehiculos(List<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos != null ? vehiculos : new ArrayList<>();
    }
}
