package com.logismart.dominio;

public class Integracion {
    private int id;
    private String tipo;
    private String proveedor;
    private String apiKey;
    private String estado;
    private int pymeId;

    public Integracion(int id, String tipo, String proveedor, String apiKey, String estado, int pymeId) {
        this.id = id;
        this.tipo = tipo;
        this.proveedor = proveedor;
        this.apiKey = apiKey;
        this.estado = estado;
        this.pymeId = pymeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getPymeId() {
        return pymeId;
    }

    public void setPymeId(int pymeId) {
        this.pymeId = pymeId;
    }


}
