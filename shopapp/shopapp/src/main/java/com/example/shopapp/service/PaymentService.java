package com.example.shopapp.service;

import com.example.shopapp.component.JwtTokenUtils;
import com.example.shopapp.dto.PaymentDTO;
import com.example.shopapp.exception.DataNotFoundException;
import com.example.shopapp.model.*;
import com.example.shopapp.repository.IPaymentRepository;
import com.example.shopapp.repository.OrderRepository;
import com.example.shopapp.repository.ProductRepository;
import com.example.shopapp.repository.UserRepository;
import com.example.shopapp.response.PaymentResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.nio.file.attribute.UserPrincipal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService{
    private final IPaymentRepository paymentRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final JwtTokenUtils jwtTokenUtil;

    private final String CLIENT_ID = "229793c4-a087-4283-8e4b-bd4952d6d2b2";
    private final String API_KEY = "248f13ca-3857-4614-be00-9c8df85e8e09";
    private final String CHECK_SUM_KEY = "10377b815b0a808741ac734c9fad441778931e13eb7c661f6cd50adbb9082427";
    private final String PARTNER_CODE = "Fl0w4rt";
    private String CANCEL_URL = "http://localhost:5173/u/payment-cancel";
    private String RETURN_URL = "http://localhost:5173/u/payment-success";

    @Override
    public PaymentResponse.PaymentData createPayment(PaymentDTO paymentDTO, String token) throws Exception {
        Order order = orderRepository.findById(paymentDTO.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find Product with id"));
        User user = userRepository.findById(paymentDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find Product with id"));
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setOrder(order);
        paymentEntity.setPaymentUser(user);
        return this.createPaymentLink(paymentEntity, token);
    }

//    @Override
    public PaymentResponse.PaymentData createPaymentLink(PaymentEntity paymentEntity, String token) throws Exception {
        String url = "https://api-merchant.payos.vn/v2/payment-requests";
        RestTemplate restTemplate = new RestTemplate();
        //Create Payment Entity
        Order order = orderRepository.findById(paymentEntity.getOrder().getId())
               .orElseThrow(() -> new DataNotFoundException("Cannot find Product with id"));

//        if (paymentEntity.getType() == OrderType.BuyNow) {
//            if(product.isBuyNow()){
//                paymentEntity.setAmount((int) product.getBuyNowPrice());
//            }else{
//                throw new AppException(HttpStatus.BAD_REQUEST,"Đơn hàng không hỗ trợ mua ngay");
//            }
//        } else {
        paymentEntity.setAmount(Math.round(order.getTotalMoney())); // Làm tròn float trước khi ép kiểu
        // Ép kiểu trực tiếp từ float sang int

//        }

        paymentEntity.setDescription("O" + order.getId());
        paymentEntity.setOrderCode(Integer.parseInt(String.valueOf(new Date().getTime()).substring(String.valueOf(new Date().getTime()).length() - 6)));

        paymentEntity = paymentRepository.save(paymentEntity);

        //Call API
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-client-id", CLIENT_ID);
        headers.set("x-api-key", API_KEY);
        headers.set("x-partner-code", PARTNER_CODE);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode paymentRequest = mapper.createObjectNode();

        paymentRequest.put("orderCode", paymentEntity.getOrderCode());
        paymentRequest.put("amount", paymentEntity.getAmount());
        paymentRequest.put("description", paymentEntity.getDescription());
        paymentRequest.put("buyerName", paymentEntity.getPaymentUser().getFullName());
        paymentRequest.put("cancelUrl", CANCEL_URL);
        paymentRequest.put("returnUrl", RETURN_URL);
        paymentRequest.put("signature", this.getSignature(paymentEntity));

        ArrayNode itemsArray = paymentRequest.putArray("items");
//        for (PaymentRequest.Item item : paymentEntity.getItems()) {
        ObjectNode itemNode = mapper.createObjectNode();
        itemNode.put("name", "Order of" + order.getUser().getFullName());
        itemNode.put("quantity", 1);
        itemNode.put("price", order.getTotalMoney());
//        itemNode.put("price", order.getPrice());
        itemsArray.add(itemNode);
//        }

        HttpEntity<ObjectNode> requestEntity = new HttpEntity<>(paymentRequest, headers);
        PaymentResponse response = restTemplate.postForObject(url, requestEntity, PaymentResponse.class);

        if (response != null && "00".equals(response.getCode())) {
            // Save payment data to the database (optional)
            paymentEntity.setCheckoutUrl(response.getData().getCheckoutUrl());
            paymentEntity.setStatus(PaymentStatus.fromString(response.getData().getStatus()));
            paymentEntity.setPaymentID(response.getData().getPaymentLinkId());
            paymentRepository.save(paymentEntity);

            // Return the checkout URL
            return response.getData();
        } else {
            throw new Exception("Payment creation failed: " + (response != null ? response.getDesc() : "Unknown error"));
        }
    }



    @Override
    public List<PaymentEntity> getPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public PaymentEntity getPayments(Long id) throws DataNotFoundException {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find Product with id"));
    }

    @Override
    public List<PaymentEntity> getMyPayments(String token) throws Exception {
        return paymentRepository.findByPaymentUserId(this.getUserDetailsFromToken(token).getId());
    }

    public User getUserDetailsFromToken(String token) throws Exception {
        if (jwtTokenUtil.isTokenExpired(token)) {
            throw new Exception("Token is expired");
        }
        String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);

        if (user.isPresent()) {
            return user.get();
        } else {
            throw new Exception("User not found");
        }
    }

    private String getSignature(PaymentEntity paymentEntity) throws Exception {
        // Concatenate the necessary fields
        String data = "amount=" + paymentEntity.getAmount()
                + "&cancelUrl=" + CANCEL_URL
                + "&description=" + paymentEntity.getDescription()
                + "&orderCode=" + paymentEntity.getOrderCode()
                + "&returnUrl=" + RETURN_URL;

        // Generate HMAC SHA256 hash
        Mac sha256HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(CHECK_SUM_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256HMAC.init(secretKey);

        // Compute the hash and encode it to hex
        byte[] hashBytes = sha256HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));

        // Convert the byte array into a hexadecimal string
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }

        // Return the hex-encoded hash (signature)
        return sb.toString();
    }

}
