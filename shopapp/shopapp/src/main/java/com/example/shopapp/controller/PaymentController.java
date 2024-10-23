package com.example.shopapp.controller;

import com.example.shopapp.dto.PaymentDTO;
import com.example.shopapp.exception.DataNotFoundException;
import com.example.shopapp.model.PaymentEntity;
import com.example.shopapp.response.PaymentResponse;
import com.example.shopapp.service.IPaymentService;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final IPaymentService paymentService;

    @PostMapping("")
    public ResponseEntity<PaymentResponse.PaymentData> createPayment(
            @Valid @RequestBody PaymentDTO request,
            @RequestHeader("Authorization") String authorizationHeader
    ) throws Exception {
        String extractedToken = authorizationHeader.substring(7);
        PaymentResponse.PaymentData paymentData = paymentService.createPayment(request, extractedToken);
        return ResponseEntity.ok()
                .body(paymentData);
    }

    @GetMapping("/me")
    public ResponseEntity<List<PaymentEntity>> getMyPayment(
            @RequestHeader("Authorization") String authorizationHeader
    ) throws Exception {
        String extractedToken = authorizationHeader.substring(7);
        return ResponseEntity.ok()
                .body(paymentService.getMyPayments(extractedToken));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<PaymentEntity>> getPayments() {
        return ResponseEntity.ok()
                .body(paymentService.getPayments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentEntity> getPayment(
            @PathVariable Long id
    ) throws DataNotFoundException {
        return ResponseEntity.ok()
                .body(paymentService.getPayments(id));
    }
}
