package com.example.shopapp.model;

import jakarta.persistence.*;
import lombok.*;

@ToString(exclude = {"user", "orderDetails"}) // Exclude user và orderDetails để tránh vòng lặp
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "payments")
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int orderCode;
    private String paymentID;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "payment_user_id")
    private User paymentUser;

//    @ManyToOne
//    @JoinColumn(name = "order_id")
//    private OrderEntity orderEntity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String checkoutUrl;

    private boolean senderTransaction;
//
//    private boolean buyNow;
//
//    private String toFullName;
//    private String toPhoneNumber;
//    private String note;
//    private String toAddress;
//    private OrderType type;
//    private String voucherCode;
}
