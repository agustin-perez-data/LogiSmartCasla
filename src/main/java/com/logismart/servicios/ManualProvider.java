package com.logismart.servicios;

import com.logismart.dominio.Envio;
import java.util.ArrayList;
import java.util.List;

public class ManualProvider implements ProveedorDeIntegracion {

    @Override
    public List<Envio> sincronizarPedidos(String apiKey) {
        System.out.println("[Manual] Cargando pedidos ingresados manualmente");
        // Stub: en produccion leeria de un CSV o formulario
        return new ArrayList<>();
    }
}
