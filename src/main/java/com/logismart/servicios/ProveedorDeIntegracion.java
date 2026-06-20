package com.logismart.servicios;

import com.logismart.dominio.Envio;
import java.util.List;

public interface ProveedorDeIntegracion {
    List<Envio> sincronizarPedidos(String apiKey);
}
