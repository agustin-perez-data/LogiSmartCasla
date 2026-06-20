package com.logismart.observer;

import com.logismart.dominio.Envio;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SistemaAuditoriaObservador implements ObservadorEnvio {

    private final List<String> registros = new ArrayList<>();

    @Override
    public void actualizar(Envio envio, String evento) {
        String entrada = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                + " | " + envio.getId()
                + " | " + evento
                + " | $" + envio.getCosto();
        registros.add(entrada);
        System.out.println("[AuditoriaObs] " + entrada);
    }

    public void mostrarRegistros() {
        System.out.println("\n=== Registros de Auditoria Observer ===");
        for (int i = 0; i < registros.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + registros.get(i));
        }
    }

    public int obtenerCantidadRegistros() {
        return registros.size();
    }
}
