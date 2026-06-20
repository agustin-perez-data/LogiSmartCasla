package com.logismart.strategy;

import com.logismart.dominio.Envio;

public class StrategyDemo {

    public static void main(String[] args) {
        Envio envio = new Envio.EnvioBuilder("ENV-001", "Buenos Aires", "Cordoba")
                .peso(5.0)
                .tipo("EXPRESS")
                .build();

        System.out.println("=== Caso 1: estrategia por defecto ===");
        imprimirCosto(envio);

        System.out.println("\n=== Caso 2: estrategia por peso ===");
        envio.establecerEstrategia(new EstrategiaPeso());
        imprimirCosto(envio);

        System.out.println("\n=== Caso 3: estrategia por urgencia ===");
        envio.establecerEstrategia(new EstrategiaUrgencia());
        imprimirCosto(envio);

        System.out.println("\n=== Caso 4: estrategia por volumen ===");
        envio.establecerEstrategia(new EstrategiaVolumen());
        imprimirCosto(envio);

        System.out.println("\n=== Caso 5: estrategia hibrida ===");
        envio.establecerEstrategia(new EstrategiaHibrida());
        imprimirCosto(envio);
    }

    private static void imprimirCosto(Envio envio) {
        System.out.println("Costo (" + envio.getTipo() + "): $" + String.format("%.2f", envio.calcularCosto()));
    }
}
