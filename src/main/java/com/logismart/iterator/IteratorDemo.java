package com.logismart.iterator;

import com.logismart.dominio.Envio;

public class IteratorDemo {

    public static void main(String[] args) {
        ColeccionEnvios coleccionArray = new ColeccionArray();
        Envio envio1 = crearEnvio("E001", "Buenos Aires", "Cordoba", 5.0, 150.0);
        Envio envio2 = crearEnvio("E002", "Rosario", "Mendoza", 8.0, 200.0);
        coleccionArray.agregar(envio1);
        coleccionArray.agregar(envio2);

        System.out.println("=== Caso 1: ColeccionArray ===");
        imprimir(coleccionArray.crearIterador(), "Array");

        ColeccionEnvios coleccionLista = new ColeccionLista();
        coleccionLista.agregar(crearEnvio("E003", "Cordoba", "Salta", 3.0, 100.0));
        coleccionLista.agregar(crearEnvio("E004", "Mendoza", "La Plata", 6.0, 180.0));

        System.out.println("\n=== Caso 2: ColeccionLista ===");
        imprimir(coleccionLista.crearIterador(), "Lista");

        ColeccionEnvios coleccionHash = new ColeccionHash();
        coleccionHash.agregar(crearEnvio("E005", "La Plata", "Junin", 7.0, 160.0));
        coleccionHash.agregar(crearEnvio("E006", "Junin", "Bahia Blanca", 4.0, 120.0));

        System.out.println("\n=== Caso 3: ColeccionHash ===");
        IteradorEnvios iterador = coleccionHash.crearIterador();
        imprimir(iterador, "Hash");

        System.out.println("\n=== Caso 4: Reiniciar iterador ===");
        iterador.reiniciar();
        imprimir(iterador, "Hash reiniciado");

        System.out.println("\n=== Caso 5: Remover y verificar tamano ===");
        System.out.println("Tamano antes: " + coleccionArray.obtenerTamano());
        coleccionArray.remover(envio1);
        System.out.println("Tamano despues: " + coleccionArray.obtenerTamano());
        imprimir(coleccionArray.crearIterador(), "Restante");
    }

    private static Envio crearEnvio(String id, String origen, String destino, double peso, double costo) {
        return new Envio.EnvioBuilder(id, origen, destino)
                .peso(peso)
                .costo(costo)
                .metodoPago("TARJETA")
                .productoId("PROD-" + id)
                .build();
    }

    private static void imprimir(IteradorEnvios iterador, String etiqueta) {
        while (iterador.tieneSiguiente()) {
            Envio envio = iterador.obtenerSiguiente();
            System.out.println(etiqueta + ": " + envio.getId() + " | "
                    + envio.getOrigen() + " -> " + envio.getDestino());
        }
    }
}
