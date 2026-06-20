package com.logismart.memento;

import com.logismart.dominio.Envio;

import java.util.ArrayList;
import java.util.List;

public class HistorialEnvios {

    private final List<MementoEnvio> historial = new ArrayList<>();
    private int indiceActual = -1;

    public void guardarEstado(Envio envio) {
        while (historial.size() > indiceActual + 1) {
            historial.remove(historial.size() - 1);
        }

        MementoEnvio memento = envio.crearMemento();
        historial.add(memento);
        indiceActual++;
        System.out.println("[Historial] Estado guardado #" + indiceActual + ": " + memento);
    }

    public boolean irAlEstadoAnterior(Envio envio) {
        if (indiceActual <= 0) {
            System.out.println("[Historial] No se puede retroceder mas");
            return false;
        }

        indiceActual--;
        MementoEnvio memento = historial.get(indiceActual);
        envio.restaurarDesdeMemento(memento);
        System.out.println("[Historial] Restaurado #" + indiceActual + ": " + memento);
        return true;
    }

    public boolean irAlEstadoSiguiente(Envio envio) {
        if (indiceActual >= historial.size() - 1) {
            System.out.println("[Historial] No se puede avanzar mas");
            return false;
        }

        indiceActual++;
        MementoEnvio memento = historial.get(indiceActual);
        envio.restaurarDesdeMemento(memento);
        System.out.println("[Historial] Avanzado #" + indiceActual + ": " + memento);
        return true;
    }

    public void mostrarHistorial() {
        System.out.println("\n=== Historial de Estados ===");
        for (int i = 0; i < historial.size(); i++) {
            String cursor = i == indiceActual ? " < actual" : "";
            System.out.println("  " + i + ". " + historial.get(i) + cursor);
        }
    }

    public int obtenerTamano() {
        return historial.size();
    }

    public int obtenerTamaño() {
        return obtenerTamano();
    }
}
