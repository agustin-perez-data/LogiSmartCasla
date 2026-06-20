package com.logismart.dominio;

public class Conductor extends Usuario {
    private boolean disponible;

    public Conductor(int id, String nombre, String email, String passwordHash, String telefono, int pymeId, boolean disponible) {
        super(id, nombre, email, passwordHash, telefono, pymeId);
        this.disponible = disponible;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}
