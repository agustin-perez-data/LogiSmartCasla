package com.logismart.facade;

public class FacadeDemo {
    public static void main(String[] args) {
        ServicioLogisticaFacade servicio = new ServicioLogisticaFacade();

        System.out.println("========== CASO 1: Crear envío exitoso ==========");
        String numero1 = servicio.crearEnvio("PROD-001", 150.0, "cliente@email.com", "+54911234567");

        System.out.println("\n========== CASO 2: Obtener estado ==========");
        if (numero1 != null) {
            String estado = servicio.obtenerEstadoEnvio(numero1);
            System.out.println("Estado del envío " + numero1 + ": " + estado);
        }

        System.out.println("\n========== CASO 3: Cancelar envío ==========");
        servicio.cancelarEnvio(numero1, "cliente@email.com");

        System.out.println("\n========== CASO 4: Stock insuficiente ==========");
        String numero2 = servicio.crearEnvio("PROD-NO-EXISTE", 150.0, "cliente@email.com", "+54911234567");
        System.out.println("Resultado: " + (numero2 == null ? "FALLIDO (esperado)" : numero2));

        System.out.println("\n========== CASO 5: Múltiples envíos ==========");
        for (int i = 1; i <= 3; i++) {
            String numero = servicio.crearEnvio("PROD-00" + i, 100.0 * i, "cliente" + i + "@email.com", "+5491123456" + i);
            System.out.println("Envío " + i + ": " + (numero != null ? numero : "FALLIDO"));
        }
    }
}
