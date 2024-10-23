package com.example.shopapp.service;

import com.example.shopapp.dto.PaymentDTO;
import com.example.shopapp.exception.DataNotFoundException;
import com.example.shopapp.model.PaymentEntity;
import com.example.shopapp.response.PaymentResponse;

import java.util.List;

public interface IPaymentService {
    PaymentResponse.PaymentData createPayment(PaymentDTO paymentDTO, String token) throws Exception;
//    PaymentResponse.PaymentData createPaymentLink(PaymentEntity paymentEntity, String token) throws Exception;

    //PaymentEntity returnValueOfPayment(PaymentResponseRequest paymentResponseRequest);

    List<PaymentEntity> getPayments();
    PaymentEntity getPayments(Long id) throws DataNotFoundException;
    List<PaymentEntity> getMyPayments(String token) throws Exception;
}
