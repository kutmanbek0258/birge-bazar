package kg.birge.bazar.searchservice.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import kg.birge.bazar.searchservice.client.ProductServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductReindexService {

    private final ProductServiceClient productServiceClient;
    private final ElasticsearchClient elasticsearchClient;

    @Value("${search.index.name:products}")
    private String indexName;

    public void reindexAllProducts() {
        List<Map<String, Object>> products = productServiceClient.getAllProducts();
        log.info("Fetched {} products from product-service", products.size());

        List<BulkOperation> operations = new ArrayList<>();
        for (Map<String, Object> product : products) {
            Object id = product.get("id");
            if (id == null) continue;

            operations.add(BulkOperation.of(b -> b
                    .index(idx -> idx
                            .index(indexName)
                            .id(String.valueOf(id))
                            .document(product)
                    )
            ));
        }

        if (!operations.isEmpty()) {
            try {
                BulkRequest bulkRequest = BulkRequest.of(b -> b.operations(operations));
                BulkResponse response = elasticsearchClient.bulk(bulkRequest);
                if (response.errors()) {
                    log.error("Bulk reindexing completed with errors: {}", response.items());
                } else {
                    log.info("Bulk reindexing completed successfully, indexed {} products", operations.size());
                }
            } catch (Exception e) {
                log.error("Failed to bulk reindex products", e);
            }
        }
    }
}