package kg.birge.bazar.searchservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import java.util.Map;

@FeignClient(name = "product-service", url = "${product-service.url}")
public interface ProductServiceClient {

    @GetMapping("/product/all") // либо эндпоинт, который возвращает все товары
    List<Map<String, Object>> getAllProducts();
}