package kg.birge.bazar.searchservice.controller;

import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/search/products")
@RequiredArgsConstructor
public class ProductSearchController {

    private final RestHighLevelClient elasticsearchClient;

    @Value("${search.index.name:products}")
    private String indexName;

    @GetMapping
    public Map<String, Object> searchProducts(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            Pageable pageable // <-- Spring автоматически подхватит page, size, sort
    ) throws Exception {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        if (query != null && !query.isBlank()) {
            boolQuery.must(QueryBuilders.multiMatchQuery(query, "title", "description"));
        }
        if (category != null && !category.isBlank()) {
            boolQuery.filter(QueryBuilders.termQuery("category", category));
        }
        if (minPrice != null) {
            boolQuery.filter(QueryBuilders.rangeQuery("price").gte(minPrice));
        }
        if (maxPrice != null) {
            boolQuery.filter(QueryBuilders.rangeQuery("price").lte(maxPrice));
        }

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(boolQuery)
                .from((int) pageable.getOffset())
                .trackTotalHits(true)
                .size(pageable.getPageSize());

        // Сортировка
        if (pageable.getSort().isSorted()) {
            for (Sort.Order order : pageable.getSort()) {
                searchSourceBuilder.sort(
                        new FieldSortBuilder(order.getProperty())
                                .order(order.isAscending() ? SortOrder.ASC : SortOrder.DESC)
                );
            }
        } else {
            // По умолчанию сортировка по дате обновления
            searchSourceBuilder.sort(new FieldSortBuilder("updatedAt").order(SortOrder.DESC));
        }

        SearchRequest searchRequest = new SearchRequest(indexName)
                .source(searchSourceBuilder);

        SearchResponse searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);

        List<Map<String, Object>> products = Arrays.stream(searchResponse.getHits().getHits())
                .map(SearchHit::getSourceAsMap)
                .collect(Collectors.toList());
        Long totalHits = searchResponse.getHits().getTotalHits().value();
        Map<String, Object> response = new HashMap<>();
        response.put("content", products);
        response.put("page", pageable.getPageNumber());
        response.put("size", pageable.getPageSize());
        response.put("totalElements", totalHits);
        response.put("totalPages", totalHits.intValue() / pageable.getPageSize());

        return response;
    }
}