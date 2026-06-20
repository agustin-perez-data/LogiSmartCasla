package com.logismart.servicios;

import com.logismart.dominio.Envio;
import java.util.ArrayList;
import java.util.List;

public class TiendanubeProvider implements ProveedorDeIntegracion {

    @Override
    public List<Envio> sincronizarPedidos(String apiKey) {
        System.out.println("[Tiendanube] Sincronizando pedidos con apiKey: " + apiKey);
        // Stub: en produccion llamaria a la API REST de Tiendanube
        return new ArrayList<>();
    }
}
