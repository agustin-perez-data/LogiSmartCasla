package com.logismart.datos.lazy;

import com.logismart.datos.dominio.Cliente;
import com.logismart.datos.repositorio.RepositorioCliente;

public class ClienteLazyProxy {
    private final int id;
    private Cliente cliente;
    private final RepositorioCliente repositorio;
    private boolean cargado = false;

    public ClienteLazyProxy(int id, RepositorioCliente repositorio) {
        this.id = id;
        this.repositorio = repositorio;
    }

    private void cargar() {
        if (!cargado) {
            this.cliente = repositorio.obtener(id);
            this.cargado = true;
            System.out.println("[LazyLoad] Cliente cargado desde repositorio: id=" + id);
        }
    }

    public String getNombre() { cargar(); return cliente != null ? cliente.getNombre() : null; }
    public String getEmail() { cargar(); return cliente != null ? cliente.getEmail() : null; }
    public String getTelefono() { cargar(); return cliente != null ? cliente.getTelefono() : null; }
    public Cliente getCliente() { cargar(); return cliente; }
    public boolean isCargado() { return cargado; }
}
