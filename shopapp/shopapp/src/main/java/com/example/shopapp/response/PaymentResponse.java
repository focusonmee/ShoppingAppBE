package com.example.shopapp.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponse {
    private String code;
    private String desc;
    private PaymentData data;

    // Getters and Setters

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PaymentData {
        private String bin;
        private String accountNumber;
        private String accountName;
        private int amount;
        private String description;
        private int orderCode;
        private String currency;
        private String paymentLinkId;
        private String status;
        private String checkoutUrl;
        private String qrCode;

        // Getters and Setters
    }
}
