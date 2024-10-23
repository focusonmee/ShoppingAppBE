package com.example.shopapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    @JsonProperty("user_id")
    @Min(value = 1, message = "User's ID must be greater than 1")
    private long userId;

    @NotBlank(message = "Full name must not be blank")
    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("email")
    private String email;

    @NotBlank(message = "Phone number must not be blank")
    @JsonProperty("phone_number")
    @Size(min = 5, message = "Phone number must be at least 5 characters")
    private String phoneNumber;

    @NotBlank(message = "Address must not be blank")
    private String address;

    @JsonProperty("status")
    private String status;

    private String note;

    @JsonProperty("total_money")
    @Min(value = 0, message = "Total must be >= 0")
    private Float totalMoney;

//    @NotBlank(message = "Shipping address must not be blank")
    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("shipping_method")
    private String shippingMethod; // Đổi tên biến thành chữ thường

    @JsonProperty("shipping_date")
    private LocalDate shippingDate;

    @JsonProperty("payment_method")
    private String paymentMethod; // Thêm ràng buộc nếu cần

    @JsonProperty("cart_items")
    private List<CartItemDTO> cartItems;

}
