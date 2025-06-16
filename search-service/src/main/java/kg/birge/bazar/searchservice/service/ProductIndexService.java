package kg.birge.bazar.searchservice.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.UpdateRequest;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductIndexService {

    private final ElasticsearchClient elasticsearchClient;
    private final ObjectMapper objectMapper;

    @Value("${search.index.name:products}")
    private String indexName;

    public void indexProduct(Map<String, Object> product) {
        try {
            String productId = String.valueOf(product.get("id"));
            elasticsearchClient.index(IndexRequest.of(r ->
                    r.index(indexName)
                            .id(productId)
                            .document(product)
            ));
            log.info("Product indexed, id={}", productId);
        } catch (ElasticsearchException | IOException e) {
            log.error("Failed to index product: {}", product, e);
        }
    }

    public void updateProduct(String productId, Map<String, Object> updates) {
        try {
            elasticsearchClient.update(UpdateRequest.of(r ->
                    r.index(indexName)
                            .id(productId)
                            .doc(updates)
            ), Map.class);
            log.info("Product updated, id={}", productId);
        } catch (ElasticsearchException | IOException e) {
            log.error("Failed to update product: id={}, updates={}", productId, updates, e);
        }
    }

    public void deleteProduct(String productId) {
        try {
            elasticsearchClient.delete(DeleteRequest.of(r ->
                    r.index(indexName)
                            .id(productId)
            ));
            log.info("Product deleted, id={}", productId);
        } catch (ElasticsearchException | IOException e) {
            log.error("Failed to delete product: id={}", productId, e);
        }
    }

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