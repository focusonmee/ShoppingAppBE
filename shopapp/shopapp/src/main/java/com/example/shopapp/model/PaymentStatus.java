package com.example.shopapp.model;

public enum PaymentStatus {
    PAID ("PAID"),
    PENDING ("PENDING"),
    PROCESSING ("PROCESSING"),
    CANCELLED ("CANCELLED");

    private String statusKey;

    PaymentStatus(String statusKey) {
        this.statusKey = statusKey;
    }

    public String getStatusKey() {
        return statusKey;
    }

    public static PaymentStatus fromString(String key) {
        for (PaymentStatus status : PaymentStatus.values()) {
            if (status.getStatusKey().equals(key)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + key);
    }
}
