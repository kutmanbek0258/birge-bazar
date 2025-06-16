package kg.birge.bazar.searchservice.listener;

import kg.birge.bazar.searchservice.service.ProductIndexService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductIndexListener {
    private final ProductIndexService productIndexService;

    @KafkaListener(topics = "product-events", groupId = "search-service-group")
    public void handleProductEvent(String message) {
        // message = JSON {eventType: "CREATED"/"UPDATED"/"DELETED", product: {...}}
        productIndexService.handleEvent(message);
    }
}