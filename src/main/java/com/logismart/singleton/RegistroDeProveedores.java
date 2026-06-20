package com.logismart.singleton;

import com.logismart.servicios.ManualProvider;
import com.logismart.servicios.MercadoLibreProvider;
import com.logismart.servicios.ProveedorDeIntegracion;
import com.logismart.servicios.TiendanubeProvider;

import java.util.HashMap;
import java.util.Map;

public class RegistroDeProveedores {

    private static volatile RegistroDeProveedores instancia;

    private final Map<String, ProveedorDeIntegracion> proveedores = new HashMap<>();

    private RegistroDeProveedores() {
        proveedores.put("MERCADOLIBRE", new MercadoLibreProvider());
        proveedores.put("TIENDANUBE",   new TiendanubeProvider());
        proveedores.put("MANUAL",       new ManualProvider());
    }

    public static RegistroDeProveedores obtenerInstancia() {
        if (instancia == null) {
            synchronized (RegistroDeProveedores.class) {
                if (instancia == null) {
                    instancia = new RegistroDeProveedores();
                }
            }
        }
        return instancia;
    }

    public ProveedorDeIntegracion obtenerProveedor(String tipo) {
        ProveedorDeIntegracion proveedor = proveedores.get(tipo.toUpperCase());
        if (proveedor == null) {
            throw new IllegalArgumentException("Proveedor no registrado: " + tipo);
        }
        return proveedor;
    }

    public void registrar(String tipo, ProveedorDeIntegracion proveedor) {
        proveedores.put(tipo.toUpperCase(), proveedor);
    }
}
