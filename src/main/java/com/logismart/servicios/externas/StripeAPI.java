package com.logismart.servicios.externas;

public class StripeAPI {
    public boolean charge(double amountInCents, String description) {
        System.out.println("[Stripe API] Charge: " + amountInCents + " cents");
        return true;
    }

    public String getChargeStatus(String chargeId) {
        return "succeeded";
    }

    public void refundCharge(String chargeId, double amountInCents) {
        System.out.println("[Stripe API] Refund " + amountInCents + " cents for " + chargeId);
    }
}
