package com.example.shopapp.service;

import com.example.shopapp.dto.OrderDTO;
import com.example.shopapp.dto.OrderDetailDTO;
import com.example.shopapp.exception.DataNotFoundException;
import com.example.shopapp.model.Order;
import com.example.shopapp.model.OrderDetail;

import java.util.List;

public interface IOrderDetailService {

    // Tạo OrderDetail từ OrderDTO
    OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception;

    // Lấy OrderDetail bằng ID Order
    OrderDetail getOrderDetail(long id) throws DataNotFoundException;

    // Tìm các OrderDetail dựa trên UserId
    List<OrderDetail> findOrderDetailsByUserId(Long userId);

    // Cập nhật OrderDetail
    OrderDetail updateOrderDetail(long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException;

    // Xóa OrderDetail
    void deleteById(long id) throws DataNotFoundException;

    List<OrderDetail> findByOrderId(Long orderId);
}
