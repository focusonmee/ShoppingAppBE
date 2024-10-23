package com.example.shopapp.schedule;

import com.example.shopapp.model.PaymentEntity;
import com.example.shopapp.model.PaymentStatus;
import com.example.shopapp.repository.IPaymentRepository;
import com.example.shopapp.repository.OrderRepository;
import com.example.shopapp.repository.ProductRepository;
import com.example.shopapp.service.IOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class PaymentSchedule {
    @Autowired
    private IPaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private IOrderService orderService;
    private final String CLIENT_ID = "229793c4-a087-4283-8e4b-bd4952d6d2b2";
    private final String API_KEY = "248f13ca-3857-4614-be00-9c8df85e8e09";
    private final String CHECK_SUM_KEY = "10377b815b0a808741ac734c9fad441778931e13eb7c661f6cd50adbb9082427";
    private final String PARTNER_CODE = "Fl0w4rt";

    @Scheduled(fixedRate = 60000)
    public void trackingPayment(){
        List<PaymentEntity> paymentEntities = paymentRepository.findAll();
        for(PaymentEntity paymentEntity : paymentEntities){
            if( paymentEntity.getStatus() != null &&
                    paymentEntity.getStatus() != PaymentStatus.CANCELLED &&
                    paymentEntity.getStatus() != PaymentStatus.PAID){

                PaymentStatus currentStatus = this.getPaymentStatus(paymentEntity.getPaymentID());
                paymentEntity.setStatus(currentStatus);
                paymentRepository.save(paymentEntity);
//                ProductEntity product = paymentEntity.getProduct();
//                OrderEntity order = orderRepository.findByProductId(product.getId());
//               if(paymentEntity.getStatus()== PaymentStatus.PAID){
////                   order.
//
//               }

            }
        }
    }

    public PaymentStatus getPaymentStatus(String id) {
        String url = "https://api-merchant.payos.vn/v2/payment-requests/" + id;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-client-id", CLIENT_ID); // Replace with actual client ID
        headers.set("x-api-key", API_KEY); // Replace with actual API key

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                // Parse the response JSON using ObjectNode
                ObjectMapper mapper = new ObjectMapper();
                ObjectNode rootNode = (ObjectNode) mapper.readTree(response.getBody());

                // Check the code for success
                String code = rootNode.get("code").asText();
                if ("00".equals(code)) {
                    // Extract payment details from the response
                    ObjectNode dataNode = (ObjectNode) rootNode.get("data");

                    return PaymentStatus.fromString(dataNode.get("status").asText());
                } else {
                    // Handle error code
                    String errorDescription = rootNode.get("desc").asText();
                    System.err.println("Error fetching payment status: " + errorDescription);
                    return null;
                }
            } else {
                System.err.println("Error: Received status code " + response.getStatusCode());
                return null;
            }

        } catch (Exception ex) {
            System.err.println("Exception occurred while fetching payment status: " + ex.getMessage());
            return null;
        }
    }
}
