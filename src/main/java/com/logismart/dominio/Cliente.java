package com.logismart.dominio;

public class Cliente extends Usuario {
    private String empresa;
    private String categoria;

    public Cliente(int id, String nombre, String email, String passwordHash,
                   String telefono, int pymeId, String empresa, String categoria) {
        super(id, nombre, email, passwordHash, telefono, pymeId);
        this.empresa = empresa;
        this.categoria = categoria;
    }

    public String getEmpresa() { return empresa; }
    public void setEmpresa(String empresa) { this.empresa = empresa; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
}
