package com.logismart.datos.repositorio;

import com.logismart.datos.dominio.Cliente;
import java.util.List;

public interface RepositorioCliente extends Repositorio<Cliente> {
    Cliente buscarPorEmail(String email);
    List<Cliente> buscarPorNombre(String nombre);
}
