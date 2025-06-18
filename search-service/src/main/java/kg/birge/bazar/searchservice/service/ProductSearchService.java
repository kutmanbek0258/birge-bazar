package kg.birge.bazar.searchservice.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

    private final ElasticsearchClient elasticsearchClient;

    @Value("${search.index.name:products}")
    private String indexName;

    public Map<String, Object> searchProducts(
            String query,
            String category,
            Double minPrice,
            Double maxPrice,
            Pageable pageable
    ) {
        try {
            // Build query
            List<Query> filters = new ArrayList<>();

            if (query != null && !query.isBlank()) {
                filters.add(Query.of(q ->
                        q.multiMatch(m -> m.query(query).fields("title", "description"))));
            }
            if (category != null && !category.isBlank()) {
                filters.add(Query.of(q ->
                        q.term(t -> t.field("category").value(category))));
            }
            if (minPrice != null || maxPrice != null) {
                filters.add(Query.of(q ->
                        q.range(r -> {
                            r.field("price");
                            if (minPrice != null) r.gte(JsonData.of(minPrice));
                            if (maxPrice != null) r.lte(JsonData.of(maxPrice));
                            return r;
                        })));
            }
            filters.add(Query.of(q -> q.term(t -> t.field("status.keyword").value("ACTIVE"))));

            Query boolQuery = filters.isEmpty()
                    ? Query.of(q -> q.matchAll(ma -> ma))
                    : Query.of(q ->
                            q.bool(b -> b.must(filters)));

            // Sort
            List<SortOptions> sortOptions = new ArrayList<>();
            if (pageable.getSort().isSorted()) {
                for (Sort.Order order : pageable.getSort()) {
                    String sortField = order.getProperty();
                    // Если сортируем по name — добавляем .keyword
                    if ("name".equals(sortField)) {
                        sortField = sortField + ".keyword";
                    }
                    String finalSortField = sortField;
                    sortOptions.add(
                            SortOptions.of(s ->
                                    s.field(f -> f
                                            .field(finalSortField)
                                            .order(order.isAscending() ? SortOrder.Asc : SortOrder.Desc)
                                    )
                            )
                    );
                }
            } else {
                sortOptions.add(
                        SortOptions.of(s ->
                                s.field(f -> f
                                        .field("name.keyword")
                                        .order(SortOrder.Asc)
                                )
                        )
                );
            }

            SearchRequest searchRequest = SearchRequest.of(s -> s
                    .index(indexName)
                    .query(boolQuery)
                    .from((int) pageable.getOffset())
                    .size(pageable.getPageSize())
                    .sort(sortOptions)
            );
            System.out.println(searchRequest.toString());

            SearchResponse<Map> searchResponse = elasticsearchClient.search(searchRequest, Map.class);
            System.out.println(searchResponse.hits().total() != null
                    ? "Total hits: " + searchResponse.hits().total().value()
                    : "No hits found");

            List products = searchResponse.hits().hits().stream()
                    .map(Hit::source)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("content", products);
            response.put("page", pageable.getPageNumber());
            response.put("size", pageable.getPageSize());
            response.put("totalElements", searchResponse.hits().total() != null
                    ? searchResponse.hits().total().value() : 0L);
            response.put("totalPages", searchResponse.hits().total() != null
                    ? (int) Math.ceil((double) searchResponse.hits().total().value() / pageable.getPageSize()) : 0);

            return response;
        } catch (IOException e) {
            throw new RuntimeException("Failed to search products", e);
        }
    }
}