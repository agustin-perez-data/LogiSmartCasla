package com.logismart.facade.subsistemas;

public class SistemaInventario {
    public boolean verificarStock(String productoId) {
        System.out.println("  [Inventario] Verificando stock de: " + productoId);
        boolean hayStock = !productoId.startsWith("PROD-NO");
        System.out.println("  [Inventario] Stock " + (hayStock ? "disponible" : "INSUFICIENTE"));
        return hayStock;
    }

    public void restarStock(String productoId, int cantidad) {
        System.out.println("  [Inventario] Restando " + cantidad + " unidad(es) de " + productoId);
    }

    public void reponerStock(String productoId, int cantidad) {
        System.out.println("  [Inventario] Reponiendo " + cantidad + " unidad(es) de " + productoId);
    }
}
