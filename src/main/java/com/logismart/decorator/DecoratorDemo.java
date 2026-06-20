package com.logismart.decorator;

public class DecoratorDemo {

    public static void main(String[] args) {

        Envio envio1 = new EnvioBasico("Buenos Aires", "Córdoba", 5.0);
        System.out.println("=== Caso 1: Envío básico ===");
        System.out.println(envio1.obtenerDescripcion());
        System.out.println("Servicios: " + envio1.obtenerServicios());
        System.out.println("Costo: $" + envio1.obtenerCosto());
        System.out.println("Tiempo: " + envio1.obtenerTiempoEntrega() + " días");

        System.out.println("\n=== Caso 2: Envío con seguro ===");
        Envio envio2 = new DecoradorSeguro(envio1);
        System.out.println("Servicios: " + envio2.obtenerServicios());
        System.out.println("Costo: $" + envio2.obtenerCosto());
        System.out.println("Tiempo: " + envio2.obtenerTiempoEntrega() + " días");

        System.out.println("\n=== Caso 3: Envío con seguro + rastreo ===");
        Envio envio3 = new DecoradorRastreoGPS(envio2);
        System.out.println("Servicios: " + envio3.obtenerServicios());
        System.out.println("Costo: $" + envio3.obtenerCosto());
        System.out.println("Tiempo: " + envio3.obtenerTiempoEntrega() + " días");

        System.out.println("\n=== Caso 4: Envío con todos los servicios ===");
        Envio envio4 = new DecoradorPrioritario(
            new DecoradorNotificacionesSMS(
                new DecoradorRastreoGPS(
                    new DecoradorSeguro(
                        new EnvioBasico("Buenos Aires", "Córdoba", 5.0)
                    )
                )
            )
        );
        System.out.println("Servicios: " + envio4.obtenerServicios());
        System.out.println("Costo: $" + envio4.obtenerCosto());
        System.out.println("Tiempo: " + envio4.obtenerTiempoEntrega() + " días");

        System.out.println("\n=== Caso 5: Comparación de costos ===");
        Envio basico    = new EnvioBasico("Buenos Aires", "Córdoba", 5.0);
        Envio conSeguro = new DecoradorSeguro(basico);
        Envio conRastreo = new DecoradorRastreoGPS(conSeguro);
        Envio conSMS    = new DecoradorNotificacionesSMS(conRastreo);
        Envio conTodo   = new DecoradorPrioritario(conSMS);

        System.out.println("Básico:                    $" + basico.obtenerCosto() + " | " + basico.obtenerTiempoEntrega() + " días");
        System.out.println("+ Seguro:                  $" + conSeguro.obtenerCosto() + " | " + conSeguro.obtenerTiempoEntrega() + " días");
        System.out.println("+ Seguro + Rastreo:        $" + conRastreo.obtenerCosto() + " | " + conRastreo.obtenerTiempoEntrega() + " días");
        System.out.println("+ Seguro + Rastreo + SMS:  $" + conSMS.obtenerCosto() + " | " + conSMS.obtenerTiempoEntrega() + " días");
        System.out.println("+ Todo:                    $" + conTodo.obtenerCosto() + " | " + conTodo.obtenerTiempoEntrega() + " días");

        System.out.println("\nSin Decorator: 16 clases (2^4 combinaciones)");
        System.out.println("Con Decorator: 7 clases, infinitas combinaciones");
    }
}
