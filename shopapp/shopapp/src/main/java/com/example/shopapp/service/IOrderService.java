package com.example.shopapp.service;

import com.example.shopapp.dto.OrderDTO;
import com.example.shopapp.exception.DataNotFoundException;
import com.example.shopapp.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderService {

    Order createOrder(OrderDTO orderDTO) throws Exception;

    Order getOrderById(long id);

    List<Order> findByUserId(Long orderId);

    Order updateOrder(long id , OrderDTO orderDTO) throws DataNotFoundException;

    void deleteOrder(long id) throws DataNotFoundException;

    public Page<Order> getOrdersByKeyword(String keyword, Pageable pageable);

    }
