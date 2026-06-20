package com.logismart.datos.repositorio;

import com.logismart.datos.dominio.EstadoPago;
import com.logismart.datos.dominio.Pago;
import java.util.List;

public interface RepositorioPago extends Repositorio<Pago> {
    List<Pago> buscarPorEnvio(int envioId);
    List<Pago> buscarPorEstado(EstadoPago estado);
}
