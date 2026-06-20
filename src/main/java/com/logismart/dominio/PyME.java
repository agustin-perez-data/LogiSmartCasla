package com.logismart.dominio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PyME {
    private int id;
    private String nombre;
    private String email;
    private String telefono;
    private String plan;
    private LocalDateTime createdAt;
    private List<Flota> flotas;
    private List<Usuario> usuarios;
    private List<Integracion> integraciones;

    public PyME(int id, String nombre, String email, String telefono, String plan, LocalDateTime createdAt) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.plan = plan;
        this.createdAt = createdAt;
        this.flotas = new ArrayList<>();
        this.usuarios = new ArrayList<>();
        this.integraciones = new ArrayList<>();
    }

    public void agregarFlota(Flota flota) {
        flotas.add(flota);
    }

    public void agregarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    public void agregarIntegracion(Integracion integracion) {
        integraciones.add(integracion);
    }

    public List<Conductor> buscarConductoresDisponibles() {
        return usuarios.stream()
            .filter(u -> u instanceof Conductor)
            .map(u -> (Conductor) u)
            .filter(Conductor::isDisponible)
            .collect(Collectors.toList());
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getPlan() { return plan; }
    public void setPlan(String plan) { this.plan = plan; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<Flota> getFlotas() { return flotas; }
    public void setFlotas(List<Flota> flotas) {
        this.flotas = flotas != null ? flotas : new ArrayList<>();
    }

    public List<Usuario> getUsuarios() { return usuarios; }
    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios != null ? usuarios : new ArrayList<>();
    }

    public List<Integracion> getIntegraciones() { return integraciones; }
    public void setIntegraciones(List<Integracion> integraciones) {
        this.integraciones = integraciones != null ? integraciones : new ArrayList<>();
    }
}
