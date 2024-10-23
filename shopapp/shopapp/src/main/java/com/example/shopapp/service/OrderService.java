package com.example.shopapp.service;

import com.example.shopapp.dto.CartItemDTO;
import com.example.shopapp.dto.OrderDTO;
import com.example.shopapp.exception.DataNotFoundException;
import com.example.shopapp.model.*;
import com.example.shopapp.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Transactional // Thêm annotation @Transactional cho phương thức này
    public Order createOrder(OrderDTO orderDTO) throws DataNotFoundException {
        // Tìm kiếm và lấy User từ repository
        User user = userRepository
                .findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with id: " + orderDTO.getUserId()));

        // Xử lý ngày giao hàng
        LocalDate shippingDate = orderDTO.getShippingDate() == null ?
                LocalDate.now() : orderDTO.getShippingDate();

        // Kiểm tra nếu ngày giao hàng trước ngày hiện tại
        if (shippingDate.isBefore(LocalDate.now())) {
            throw new DataNotFoundException("Shipping date must be at least today!");
        }

        // Tạo một đối tượng Order mới
        Order order = new Order();
        // Ánh xạ OrderDTO sang Order, bỏ qua việc ánh xạ cho ID
        modelMapper.map(orderDTO, order);

        // Gán thông tin người dùng và các giá trị khác
        order.setUser(user);
        order.setOrderDate(LocalDate.now());
        order.setStatus(OrderStatus.PENDING);
        order.setIsActive(true);
        order.setShippingDate(shippingDate);
        order.setTotalMoney(orderDTO.getTotalMoney());

        // Lưu order
        order = orderRepository.save(order);

        // Tạo danh sách OrderDetails và lưu chúng
        List<OrderDetail> orderDetails = new ArrayList<>();
        for(CartItemDTO cartItemDTO: orderDTO.getCartItems()){
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            Long productId = cartItemDTO.getProductId();
            int quantity = cartItemDTO.getQuantity();

            Product product = productRepository.findById(productId)
                    .orElseThrow(()-> new DataNotFoundException("Product not found with id: "+productId));

            orderDetail.setProduct(product);
            orderDetail.setNumberOfProducts(quantity);
            orderDetail.setPrice(product.getPrice());

            // Lưu orderDetail
            orderDetailRepository.save(orderDetail);
            orderDetails.add(orderDetail);
        }

        // Trả về đối tượng Order sau khi đã được lưu
        return order;
    }


    @Override
    @Transactional // Thêm annotation @Transactional cho phương thức này
    public Order getOrderById(long id) {
        return orderRepository.findById(id).orElseThrow(null);
    }

    @Override
    @Transactional // Thêm annotation @Transactional cho phương thức này
    public List<Order> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    @Transactional // Thêm annotation @Transactional cho phương thức này
    public Order updateOrder(long id, OrderDTO orderDTO) throws DataNotFoundException {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Can not find order with id " + id));
        User existingUser = userRepository.findById(orderDTO.getUserId()).orElseThrow(
                () -> new DataNotFoundException("Can not find user with id: "+orderDTO.getUserId()));

//        modelMapper.map(orderDTO, order);
        order.setFullName(orderDTO.getFullName());
        order.setEmail(orderDTO.getEmail());
        order.setPhoneNumber(orderDTO.getPhoneNumber());
        order.setAddress(orderDTO.getAddress());
        order.setStatus(orderDTO.getStatus());
        order.setNote(orderDTO.getNote());
        order.setTotalMoney(orderDTO.getTotalMoney());
        order.setShippingAddress(orderDTO.getShippingAddress());
        order.setShippingMethod(orderDTO.getShippingMethod());
        order.setShippingDate(orderDTO.getShippingDate());
        order.setPaymentMethod(orderDTO.getPaymentMethod());
        order.setUser(existingUser);
        return orderRepository.save(order);
    }

    @Transactional // Thêm annotation @Transactional cho phương thức này
    @Override
    public void deleteOrder(long id) throws DataNotFoundException {
        Order order = orderRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Can not find order with id" + id));
        if(order != null) {
            order.setIsActive(false);
            orderRepository.save(order);
        }
    }

    @Override
    public Page<Order> getOrdersByKeyword(String keyword, Pageable pageable) {
        return orderRepository.findByKeyword(keyword, pageable);
    }

}
