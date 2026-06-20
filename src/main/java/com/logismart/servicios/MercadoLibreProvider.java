package com.logismart.servicios;

import com.logismart.dominio.Envio;
import java.util.ArrayList;
import java.util.List;

public class MercadoLibreProvider implements ProveedorDeIntegracion {

    @Override
    public List<Envio> sincronizarPedidos(String apiKey) {
        System.out.println("[MercadoLibre] Sincronizando pedidos con apiKey: " + apiKey);
        // Stub: en produccion llamaria a la API REST de MercadoLibre
        return new ArrayList<>();
    }
}
