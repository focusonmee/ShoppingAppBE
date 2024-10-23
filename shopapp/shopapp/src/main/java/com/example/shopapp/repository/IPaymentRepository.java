package com.example.shopapp.repository;

import com.example.shopapp.model.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPaymentRepository extends JpaRepository<PaymentEntity, Long> {
    List<PaymentEntity> findByPaymentID(String paymentID);
    List<PaymentEntity> findByPaymentUserId(Long paymentUserId);
}
