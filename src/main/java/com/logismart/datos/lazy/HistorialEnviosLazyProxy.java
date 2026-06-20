package com.logismart.datos.lazy;

import com.logismart.datos.dominio.Envio;
import com.logismart.datos.repositorio.RepositorioEnvio;
import java.util.List;

public class HistorialEnviosLazyProxy {
    private final int clienteId;
    private List<Envio> historial;
    private final RepositorioEnvio repositorio;
    private boolean cargado = false;

    public HistorialEnviosLazyProxy(int clienteId, RepositorioEnvio repositorio) {
        this.clienteId = clienteId;
        this.repositorio = repositorio;
    }

    private void cargar() {
        if (!cargado) {
            this.historial = repositorio.obtenerTodos();
            this.cargado = true;
            System.out.println("[LazyLoad] Historial de envios cargado para cliente id=" + clienteId);
        }
    }

    public List<Envio> obtener() { cargar(); return historial; }
    public int contar() { cargar(); return historial != null ? historial.size() : 0; }
    public boolean isCargado() { return cargado; }
}
