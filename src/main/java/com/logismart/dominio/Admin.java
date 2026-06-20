package com.logismart.dominio;

public class Admin extends Usuario {
    private int nivelAcceso;

    public Admin(int id, String nombre, String email, String passwordHash,
                 String telefono, int pymeId, int nivelAcceso) {
        super(id, nombre, email, passwordHash, telefono, pymeId);
        this.nivelAcceso = nivelAcceso;
    }

    public int getNivelAcceso() { return nivelAcceso; }
    public void setNivelAcceso(int nivelAcceso) { this.nivelAcceso = nivelAcceso; }
}
