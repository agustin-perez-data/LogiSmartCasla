package com.logismart.servicios.externas;

public class FedExAPI {
    public int crearShipment(String from, String to, double weight) {
        System.out.println("[FedEx API] Creating shipment: " + from + " → " + to);
        return (int) (System.currentTimeMillis() % 1000000);
    }

    public String getShipmentStatus(int shipmentId) {
        return "DELIVERED";
    }

    public float getShippingRate(String from, String to, double weight) {
        return (float) (weight * 12.0);
    }
}
