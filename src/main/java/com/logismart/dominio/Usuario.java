package com.logismart.dominio;

public abstract class Usuario {
    private int id;
    private String nombre;
    private String email;
    private String passwordHash;
    private String telefono;
    private int pymeId;

    public Usuario(int id, String nombre, String email, String passwordHash, String telefono, int pymeId) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.passwordHash = passwordHash;
        this.telefono = telefono;
        this.pymeId = pymeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getPymeId() {
        return pymeId;
    }

    public void setPymeId(int pymeId) {
        this.pymeId = pymeId;
    }


}
