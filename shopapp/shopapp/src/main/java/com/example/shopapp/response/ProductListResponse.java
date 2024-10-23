package com.example.shopapp.response;

import lombok.*;

import java.util.List;

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductListResponse {
    private List<ProductResponse> products; // Danh sách sản phẩm
    private int totalPages;                 // Tổng số trang
}
