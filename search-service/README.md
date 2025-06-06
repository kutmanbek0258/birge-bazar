# Search Service

Микросервис полнотекстового поиска товаров на базе Elasticsearch.

- Индексирует и ищет товары по данным из product-service.
- Поддерживает событийное обновление каталога через Kafka.
- Для поиска использует Spring Data Elasticsearch.
- Поиск по маршруту: `/api/search?q=...`

## Основные зависимости

- Spring Boot
- Spring Data Elasticsearch
- Spring Cloud OpenFeign (для product-service)
- Kafka (для событий о продуктах)