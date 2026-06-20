package com.logismart.mediator;

import java.util.ArrayList;
import java.util.List;

public class SistemaAuditoria {

    private final List<String> logs = new ArrayList<>();

    public void registrar(String evento, Object datos) {
        String log = "[Auditoria] " + evento + " | " + datos;
        logs.add(log);
        System.out.println(log);
    }

    public void mostrarLogs() {
        System.out.println("\n=== Logs de Auditoria Mediator ===");
        for (int i = 0; i < logs.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + logs.get(i));
        }
    }

    public int obtenerCantidadLogs() {
        return logs.size();
    }
}
