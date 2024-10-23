//package com.example.shopapp.model;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonManagedReference;
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.Date;
//import java.util.List;
//
//@ToString
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Builder
//@Table(name = "orders")
//public class Order {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long id;
//
//    @ManyToOne
//    @JoinColumn(name ="user_id")
//    @JsonBackReference // Ngăn vòng lặp với Order
//    private User user;
//
//    @Column(name = "fullname", length = 100)
//    private String fullName;
//
//    @Column(name = "email", length = 150)
//    private String email;
//
//    @Column(name = "phone_number", nullable = false, length = 10)
//    private String phoneNumber;
//
//    @Column(name = "address", length = 255)
//    private String address;
//
//    @Column(name = "note", length = 100)
//    private String note;
//
//    @Column(name = "order_date")
//    private LocalDate  orderDate;
//
//    @Column(name = "status")
//    private String status;
//
//    @Column(name = "total_money")
//    private Float totalMoney;
//
//    @Column(name = "shipping_method")
//    private String shippingMethod;
//
//    @Column(name = "shipping_address")
//    private String shippingAddress;
//
//    @Column(name = "shipping_date")
//    private LocalDate shippingDate;
//
//    @Column(name = "tracking_number")
//    private String trackingNumber;
//
//    @Column(name = "payment_method")
//    private String paymentMethod;
//
//
//
//    // Đúng tên cột "is_active" như trong cơ sở dữ liệu
//    @Column(name = "is_active")
//    private Boolean isActive;
//
//    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JsonManagedReference
//    @JsonIgnore // Ngăn không cho ánh xạ
//    private List<OrderDetail> orderDetails;
//}
package com.example.shopapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@ToString(exclude = {"user", "orderDetails"}) // Exclude user và orderDetails để tránh vòng lặp
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name ="user_id")
    @JsonBackReference // Ngăn vòng lặp với Order
    private User user;

    @Column(name = "fullname", length = 100)
    private String fullName;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "phone_number", nullable = false, length = 10)
    private String phoneNumber;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "note", length = 100)
    private String note;

    @Column(name = "order_date")
    private LocalDate orderDate;

    @Column(name = "status")
    private String status;

    @Column(name = "total_money")
    private Float totalMoney;

    @Column(name = "shipping_method")
    private String shippingMethod;

    @Column(name = "shipping_address")
    private String shippingAddress;

    @Column(name = "shipping_date")
    private LocalDate shippingDate;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<OrderDetail> orderDetails; // Bỏ @JsonIgnore ở đây, vì bạn có thể muốn ánh xạ orderDetails khi cần
}
