package kg.birge.bazar.paymentservice.controller;

import kg.birge.bazar.paymentservice.dto.PaymentRequestDto;
import kg.birge.bazar.paymentservice.dto.PaymentResponseDto;
import kg.birge.bazar.paymentservice.service.MockPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final MockPaymentService mockPaymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDto> processPayment(@RequestBody PaymentRequestDto request) {
        PaymentResponseDto response = mockPaymentService.processPayment(request);
        return ResponseEntity.ok(response);
    }
}
