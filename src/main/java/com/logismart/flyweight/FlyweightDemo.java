package com.logismart.flyweight;

import java.util.ArrayList;
import java.util.List;

public class FlyweightDemo {
    public static void main(String[] args) {

        System.out.println("=== Caso 1: Crear ubicaciones ===");
        Ubicacion ba1 = FabricaUbicaciones.obtener("Buenos Aires", "Buenos Aires", "1425");
        Ubicacion ba2 = FabricaUbicaciones.obtener("Buenos Aires", "Buenos Aires", "1425");
        Ubicacion cba = FabricaUbicaciones.obtener("Córdoba", "Córdoba", "5000");

        System.out.println("\n=== Caso 2: Verificar identidad ===");
        System.out.println("ba1 == ba2: " + (ba1 == ba2));
        System.out.println("ba1.equals(ba2): " + ba1.equals(ba2));

        System.out.println("\n=== Caso 3: Crear 10,000 referencias ===");
        for (int i = 0; i < 10000; i++) {
            FabricaUbicaciones.obtener("Buenos Aires", "Buenos Aires", "1425");
            FabricaUbicaciones.obtener("Córdoba", "Córdoba", "5000");
            FabricaUbicaciones.obtener("Rosario", "Santa Fe", "2000");
        }
        System.out.println("10,000 referencias pero solo 3 objetos en memoria");

        System.out.println("\n=== Caso 4: Estadísticas ===");
        FabricaUbicaciones.mostrarEstadisticas();

        System.out.println("\n=== Caso 5: Comparación sin Flyweight ===");
        List<Ubicacion> sinFlyweight = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            sinFlyweight.add(new Ubicacion("Buenos Aires", "Buenos Aires", "1425"));
        }
        System.out.println("Sin Flyweight: 1000 objetos en memoria");
        System.out.println("Con Flyweight: 1 objeto en memoria");
        System.out.println("Ahorro de memoria: 99.9%");
    }
}
