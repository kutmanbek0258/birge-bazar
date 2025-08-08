package kg.birge.bazar.paymentservice.service;

import kg.birge.bazar.paymentservice.dto.PaymentRequestDto;
import kg.birge.bazar.paymentservice.dto.PaymentResponseDto;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class MockPaymentService {

    @SneakyThrows
    public PaymentResponseDto processPayment(PaymentRequestDto request) {
        // 1. Simulate network delay
        Thread.sleep(2000);

        // 2. Simulate a 95% success rate
        boolean isSuccess = ThreadLocalRandom.current().nextInt(100) < 95;

        String status = isSuccess ? "SUCCESS" : "FAILED";
        String paymentId = UUID.randomUUID().toString();

        // In a real application, you would now save the transaction to the database
        // and publish an event to Kafka (e.g., "payment-processed").

        return new PaymentResponseDto(paymentId, status);
    }
}
