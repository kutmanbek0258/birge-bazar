package kg.birge.bazar.searchservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductIndexService {

    private final RestHighLevelClient elasticsearchClient;
    private final ObjectMapper objectMapper;

    @Value("${search.index.name:products}")
    private String indexName;

    /**
     * Индексирует (добавляет или обновляет) товар в Elasticsearch
     */
    public void indexProduct(Map<String, Object> product) {
        try {
            String productId = String.valueOf(product.get("id"));
            IndexRequest request = new IndexRequest(indexName)
                    .id(productId)
                    .source(product);
            elasticsearchClient.index(request, RequestOptions.DEFAULT);
            log.info("Product indexed, id={}", productId);
        } catch (Exception e) {
            log.error("Failed to index product: {}", product, e);
        }
    }

    /**
     * Обновляет товар (patch) в Elasticsearch
     */
    public void updateProduct(String productId, Map<String, Object> updates) {
        try {
            UpdateRequest request = new UpdateRequest(indexName, productId)
                    .doc(updates);
            elasticsearchClient.update(request, RequestOptions.DEFAULT);
            log.info("Product updated, id={}", productId);
        } catch (Exception e) {
            log.error("Failed to update product: id={}, updates={}", productId, updates, e);
        }
    }

    /**
     * Удаляет товар из индекса Elasticsearch
     */
    public void deleteProduct(String productId) {
        try {
            DeleteRequest request = new DeleteRequest(indexName, productId);
            elasticsearchClient.delete(request, RequestOptions.DEFAULT);
            log.info("Product deleted, id={}", productId);
        } catch (Exception e) {
            log.error("Failed to delete product: id={}", productId, e);
        }
    }

    /**
     * Обработка события из Kafka (создание/обновление/удаление)
     */
    public void handleEvent(String eventJson) {
        try {
            Map<String, Object> event = objectMapper.readValue(eventJson, Map.class);
            String eventType = (String) event.get("eventType");
            Map<String, Object> product = (Map<String, Object>) event.get("product");
            if ("CREATED".equals(eventType) || "UPDATED".equals(eventType)) {
                indexProduct(product);
            } else if ("DELETED".equals(eventType)) {
                String productId = String.valueOf(product.get("id"));
                deleteProduct(productId);
            }
        } catch (Exception e) {
            log.error("Failed to handle product event: {}", eventJson, e);
        }
    }
}