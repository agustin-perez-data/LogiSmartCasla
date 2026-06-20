package com.logismart.dominio;

public class Operador extends Usuario {
    private String sector;

    public Operador(int id, String nombre, String email, String passwordHash, String telefono, int pymeId, String sector) {
        super(id, nombre, email, passwordHash, telefono, pymeId);
        this.sector = sector;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }
}
