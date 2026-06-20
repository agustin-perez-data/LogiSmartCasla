package com.logismart.factory;

import com.logismart.servicios.ProveedorDeIntegracion;
import com.logismart.singleton.RegistroDeProveedores;

public class ProveedorDeIntegracionFactory {

    public static ProveedorDeIntegracion crearProveedor(String tipo) {
        return RegistroDeProveedores.obtenerInstancia().obtenerProveedor(tipo);
    }
}
