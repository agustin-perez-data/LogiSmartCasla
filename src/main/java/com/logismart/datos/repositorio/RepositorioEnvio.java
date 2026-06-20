package com.logismart.datos.repositorio;

import com.logismart.datos.dominio.Envio;
import com.logismart.datos.dominio.EstadoEnvio;
import java.util.List;

public interface RepositorioEnvio extends Repositorio<Envio> {
    List<Envio> buscarPorEstado(EstadoEnvio estado);
    List<Envio> buscarPorOrigen(String origen);
    List<Envio> buscarPorDestino(String destino);
}
