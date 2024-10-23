package com.example.shopapp.repository;

import com.example.shopapp.model.Order;
import com.example.shopapp.model.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUserId (Long orderId);

//@Query("SELECT o FROM Order o WHERE " +
//        "(:keyword IS NULL OR :keyword = '' OR " +
//        "o.fullName LIKE %:keyword% OR " +
//        "o.address LIKE %:keyword% OR " +
//        "o.note LIKE %:keyword% OR " +
//        "o.email LIKE %:keyword%)")
@Query("SELECT o FROM Order o WHERE " +
        "(:keyword IS NULL OR :keyword = '' OR " +
        "o.fullName LIKE %:keyword% OR " +
        "o.address LIKE %:keyword% OR " +
        "o.note LIKE %:keyword% OR " +
        "o.email LIKE %:keyword%) " +
        "AND o.isActive = true ")
Page<Order> findByKeyword(String keyword, Pageable pageable);


}
