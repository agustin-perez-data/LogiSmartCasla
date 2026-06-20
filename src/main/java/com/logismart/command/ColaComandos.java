package com.logismart.command;

import java.util.ArrayList;
import java.util.List;

public class ColaComandos {

    private final List<Comando> historial = new ArrayList<>();
    private int indiceActual = -1;

    public void ejecutar(Comando comando) {
        // Descarta cualquier comando que fue deshecho antes de ejecutar uno nuevo
        while (indiceActual < historial.size() - 1) {
            historial.remove(historial.size() - 1);
        }
        comando.ejecutar();
        historial.add(comando);
        indiceActual++;
    }

    public void deshacer() {
        if (indiceActual >= 0) {
            Comando comando = historial.get(indiceActual);
            comando.deshacer();
            indiceActual--;
            System.out.println("✓ Deshecho: " + comando.obtenerDescripcion());
        } else {
            System.out.println("  (nada que deshacer)");
        }
    }

    public void rehacer() {
        if (indiceActual < historial.size() - 1) {
            indiceActual++;
            Comando comando = historial.get(indiceActual);
            comando.ejecutar();
            System.out.println("✓ Rehecho: " + comando.obtenerDescripcion());
        } else {
            System.out.println("  (nada que rehacer)");
        }
    }

    public void mostrarHistorial() {
        System.out.println("\n=== Historial de Comandos ===");
        for (int i = 0; i < historial.size(); i++) {
            String marca = (i == indiceActual) ? "→" : " ";
            System.out.println(marca + " " + (i + 1) + ". " + historial.get(i).obtenerDescripcion());
        }
        System.out.println();
    }

    public int obtenerTamaño() {
        return historial.size();
    }
}
