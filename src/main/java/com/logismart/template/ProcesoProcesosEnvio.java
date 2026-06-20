package com.logismart.template;

import com.logismart.dominio.Envio;

public abstract class ProcesoProcesosEnvio {

    public final void procesarEnvio(Envio envio) {
        System.out.println("[Proceso] Iniciando procesamiento de " + envio.getId());
        validar(envio);
        calcularCosto(envio);
        procesarPago(envio);
        notificar(envio);
        System.out.println("[Proceso] Procesamiento completado\n");
    }

    protected abstract void validar(Envio envio);
    protected abstract void calcularCosto(Envio envio);
    protected abstract void procesarPago(Envio envio);
    protected abstract void notificar(Envio envio);
}
