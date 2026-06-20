package com.logismart.datos.repositorio;

import java.util.List;

public interface Repositorio<T> {
    void guardar(T entidad);
    void actualizar(T entidad);
    void eliminar(T entidad);
    T obtener(int id);
    List<T> obtenerTodos();
}
