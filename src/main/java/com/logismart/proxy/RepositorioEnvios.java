package com.logismart.proxy;

import java.util.List;

public interface RepositorioEnvios {
    EnvioSimple obtenerEnvio(String id);
    List<EnvioSimple> obtenerEnvios();
    void guardarEnvio(EnvioSimple envio);
    void eliminarEnvio(String id);
}
