package com.logismart.factory;

import com.logismart.dominio.Admin;
import com.logismart.dominio.Cliente;
import com.logismart.dominio.Conductor;
import com.logismart.dominio.Operador;
import com.logismart.dominio.TipoUsuario;
import com.logismart.dominio.Usuario;
import com.logismart.singleton.LoggerLogiSmart;

public class UsuarioFactory {

    public static Usuario crearUsuario(TipoUsuario tipo, int id, String nombre,
                                       String email, String passwordHash,
                                       String telefono, int pymeId,
                                       String extra) {
        LoggerLogiSmart logger = LoggerLogiSmart.obtenerInstancia();
        Usuario usuario = switch (tipo) {
            case CLIENTE   -> new Cliente(id, nombre, email, passwordHash, telefono, pymeId,
                                          extra, "REGULAR");
            case OPERADOR  -> new Operador(id, nombre, email, passwordHash, telefono, pymeId,
                                           extra);
            case CONDUCTOR -> new Conductor(id, nombre, email, passwordHash, telefono, pymeId,
                                            true);
            case ADMIN     -> new Admin(id, nombre, email, passwordHash, telefono, pymeId, 1);
        };
        logger.info("Usuario creado: " + tipo + " | nombre=" + nombre + " | email=" + email);
        return usuario;
    }
}
