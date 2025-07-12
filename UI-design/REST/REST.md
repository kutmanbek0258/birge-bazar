Опираясь на предоставленные описания страниц, сформирую REST API эндпоинты для каждой из них в формате Markdown.

-----

### **REST API Эндпоинты для Фронтенда Маркетплейса "Бирге Базар"**

-----

#### **1. Общие и Покупательские Страницы**

##### **1.1. Главная страница (Homepage)**

* **Получение данных для главной страницы:**
    * `GET /api/home/content`
    * **Микросервис:** `Content Service` / `Product Service` (агрегация данных через API Gateway)
    * **Описание:** Возвращает данные для отображения баннеров, витрин товаров (популярные, новинки, акционные), категорий.

##### **1.2. Страница Категории/Подкатегории**

* **Получение товаров по категории:**
    * `GET /api/products/category/{categoryId}`
    * **Микросервис:** `Product Service` / `Search Service`
    * **Описание:** Возвращает список товаров, принадлежащих указанной категории. Поддерживает параметры для пагинации, сортировки, фильтрации.
    * **Пример:** `/api/products/category/123?page=0&size=20&sort=price,asc&minPrice=100&maxPrice=1000`

##### **1.3. Страница Товара (Product Detail Page - PDP)**

* **Получение деталей товара:**
    * `GET /api/products/{productId}`
    * **Микросервис:** `Product Service`
    * **Описание:** Возвращает полную информацию о товаре, включая описание, характеристики, варианты, информацию о продавце, ссылки на изображения.
* **Получение отзывов товара:**
    * `GET /api/reviews/product/{productId}`
    * **Микросервис:** `Review Service`
    * **Описание:** Получает список отзывов для данного товара. Поддерживает пагинацию и сортировку.
* **Оставление отзыва на товар (для аутентифицированных пользователей):**
    * `POST /api/reviews/product/{productId}`
    * **Микросервис:** `Review Service`
    * **Описание:** Позволяет аутентифицированному пользователю оставить отзыв о товаре.
    * **Авторизация:** Требуется (JWT)
    * **Тело запроса:**
      ```json
      {
        "rating": 5,
        "comment": "Отличный товар, очень доволен!",
        "images": ["url_to_image1.jpg"]
      }
      ```
* **Получение вопросов и ответов (Q\&A):**
    * `GET /api/qa/product/{productId}`
    * **Микросервис:** `QA Service` (новый сервис)
    * **Описание:** Получает список вопросов и ответов для данного товара.
* **Задать вопрос (для аутентифицированных пользователей):**
    * `POST /api/qa/product/{productId}/question`
    * **Микросервис:** `QA Service`
    * **Описание:** Позволяет пользователю задать вопрос о товаре.
    * **Авторизация:** Требуется (JWT)

##### **1.4. Страница Поиска (Search Results Page)**

* **Выполнение поиска:**
    * `GET /api/search/products`
    * **Микросервис:** `Search Service`
    * **Описание:** Выполняет поиск товаров по заданному запросу. Поддерживает фильтры, сортировку, пагинацию.
    * **Пример:** `/api/search/products?query=смартфон&brand=Samsung&minPrice=5000`

##### **1.5. Корзина (Shopping Cart Page)**

* **Получение содержимого корзины:**
    * `GET /api/cart/me`
    * **Микросервис:** `Cart Service`
    * **Описание:** Возвращает текущее содержимое корзины пользователя.
    * **Авторизация:** Опционально (позволяет хранить корзину для неавторизованных, но при авторизации связывает с пользователем).
* **Добавление товара в корзину:**
    * `POST /api/cart/me/items`
    * **Микросервис:** `Cart Service`
    * **Описание:** Добавляет товар в корзину.
    * **Тело запроса:**
      ```json
      {
        "productId": "product_id_123",
        "quantity": 1,
        "variantId": "variant_id_456" // Если есть варианты
      }
      ```
* **Обновление количества товара в корзине:**
    * `PUT /api/cart/me/items/{itemId}`
    * **Микросервис:** `Cart Service`
    * **Описание:** Изменяет количество конкретного товара в корзине.
    * **Тело запроса:**
      ```json
      {
        "quantity": 3
      }
      ```
* **Удаление товара из корзины:**
    * `DELETE /api/cart/me/items/{itemId}`
    * **Микросервис:** `Cart Service`
    * **Описание:** Удаляет товар из корзины.
* **Применение промокода:**
    * `POST /api/cart/me/apply-promo`
    * **Микросервис:** `Cart Service` / `Promotion Service`
    * **Описание:** Применяет промокод к содержимому корзины.
    * **Тело запроса:**
      ```json
      {
        "promoCode": "BIRGEBAZAR2024"
      }
      ```

##### **1.6. Страницы Оформления Заказа (Checkout Pages)**

* **Инициализация оформления заказа / Просмотр сводки:**
    * `GET /api/checkout/summary`
    * **Микросервис:** `Order Service` / `Cart Service`
    * **Описание:** Получает сводную информацию о заказе перед его подтверждением (товары, общая сумма, примененные скидки).
* **Выбор/создание адреса доставки (если не было на странице профиля):**
    * `POST /api/orders/checkout/address`
    * **Микросервис:** `Order Service` / `User Service`
    * **Описание:** Указывает или создает адрес доставки для текущего оформляемого заказа.
    * **Тело запроса:** (аналогично созданию адреса в профиле)
* **Выбор способа доставки:**
    * `POST /api/orders/checkout/shipping-method`
    * **Микросервис:** `Order Service` / `Logistics Service`
    * **Описание:** Выбирает способ доставки для заказа (курьер, самовывоз). Может рассчитать стоимость и сроки.
    * **Тело запроса:**
      ```json
      {
        "methodId": "courier_delivery",
        "pickupPointId": "pickup_point_123" // Если самовывоз
      }
      ```
* **Выбор способа оплаты:**
    * `POST /api/orders/checkout/payment-method`
    * **Микросервис:** `Order Service` / `Payment Service`
    * **Описание:** Выбирает способ оплаты для заказа (онлайн-карта, наложенный платеж и т.д.).
    * **Тело запроса:**
      ```json
      {
        "methodType": "ONLINE_CARD",
        "cardToken": "stripe_card_token_xyz" // Или ID сохраненной карты
      }
      ```
* **Подтверждение и создание заказа:**
    * `POST /api/orders/checkout/confirm`
    * **Микросервис:** `Order Service`
    * **Описание:** Финализирует процесс оформления, создавая заказ в системе.
    * **Авторизация:** Требуется (JWT)
    * **Тело запроса:** (минимум данных, т.к. большая часть уже выбрана на предыдущих шагах)
      ```json
      {
        "paymentConfirmationRequired": false // Если оплата уже прошла или наложенный платеж
      }
      ```
* **Проверка статуса заказа (после успешного оформления):**
    * `GET /api/orders/{orderId}/status`
    * **Микросервис:** `Order Service` / `Tracking Service`
    * **Описание:** Получает текущий статус созданного заказа.

##### **1.7. Личный Кабинет Покупателя (Buyer Personal Account)**

* См. подробное описание эндпоинтов в предыдущем ответе (для `/profile/info`, `/profile/orders`, `/profile/favorites`, `/profile/reviews`, `/profile/addresses`, `/profile/payments`, `/profile/support`, `/profile/notifications`, `/profile/security`).

##### **1.8. Страница Продавца/Магазина (Seller Store Page)**

* **Получение информации о продавце:**
    * `GET /api/sellers/{sellerId}`
    * **Микросервис:** `User Service` / `Seller Service`
    * **Описание:** Получает публичную информацию о продавце (название магазина, описание, рейтинг, контакты).
* **Получение товаров продавца:**
    * `GET /api/products/seller/{sellerId}`
    * **Микросервис:** `Product Service` / `Search Service`
    * **Описание:** Получает список всех товаров, продаваемых данным продавцом.

-----

#### **2. Панель Продавца (Seller Panel)**

* **Дашборд Продавца (Seller Dashboard):**

    * `GET /api/sellers/me/dashboard-stats`
    * **Микросервис:** `Seller Service` / `Order Service` / `Analytics Service` (агрегация)
    * **Описание:** Возвращает ключевые метрики и статистику для дашборда продавца.
    * **Авторизация:** Требуется (JWT), роль `SELLER`

* **Управление Товарами (Product Management):**

    * `GET /api/sellers/me/products`
    * **Микросервис:** `Product Service`
    * **Описание:** Получает список всех товаров текущего продавца.
    * `POST /api/sellers/me/products`
    * **Микросервис:** `Product Service`
    * **Описание:** Создает новый товар.
    * `PUT /api/sellers/me/products/{productId}`
    * **Микросервис:** `Product Service`
    * **Описание:** Обновляет существующий товар.
    * `DELETE /api/sellers/me/products/{productId}`
    * **Микросервис:** `Product Service`
    * **Описание:** Удаляет товар.

* **Управление Заказами (Order Management):**

    * `GET /api/orders/seller/my-orders`
    * **Микросервис:** `Order Service`
    * **Описание:** Получает список заказов, относящихся к текущему продавцу.
    * `PUT /api/orders/{orderId}/status`
    * **Микросервис:** `Order Service`
    * **Описание:** Изменяет статус заказа (например, "принят", "собран", "готов к отгрузке").
    * **Тело запроса:** `{ "newStatus": "PROCESSED" }`

* **Отзывы (Reviews):**

    * `GET /api/reviews/seller/my-reviews`
    * **Микросервис:** `Review Service`
    * **Описание:** Получает список отзывов, оставленных на товары текущего продавца.
    * `POST /api/reviews/{reviewId}/reply`
    * **Микросервис:** `Review Service`
    * **Описание:** Позволяет продавцу ответить на отзыв покупателя.

* **Сообщения (Messages/Чат):**

    * `GET /api/messages/seller/chats`
    * **Микросервис:** `Messaging Service`
    * **Описание:** Получает список всех диалогов продавца с покупателями.
    * `GET /api/messages/seller/chats/{chatId}/messages`
    * **Микросервис:** `Messaging Service`
    * **Описание:** Получает историю сообщений в конкретном чате.
    * `POST /api/messages/seller/chats/{chatId}/messages`
    * **Микросервис:** `Messaging Service`
    * **Описание:** Отправляет сообщение в чат.

* **Аналитика (Analytics):**

    * `GET /api/analytics/seller/sales`
    * **Микросервис:** `Analytics Service`
    * **Описание:** Получает данные по продажам за период.
    * `GET /api/analytics/seller/product-views`
    * **Микросервис:** `Analytics Service`
    * **Описание:** Получает статистику просмотров товаров продавца.

* **Финансы (Finance/Payouts):**

    * `GET /api/finance/seller/balance`
    * **Микросервис:** `Finance Service`
    * **Описание:** Получает текущий баланс продавца.
    * `GET /api/finance/seller/payouts`
    * **Микросервис:** `Finance Service`
    * **Описание:** Получает историю выплат.
    * `POST /api/finance/seller/request-payout`
    * **Микросервис:** `Finance Service`
    * **Описание:** Запрашивает выплату средств.

* **Реклама и продвижение (Promotions/Ads):**

    * `GET /api/promotions/seller/my-promotions`
    * **Микросервис:** `Promotion Service`
    * **Описание:** Получает список активных и завершенных акций продавца.
    * `POST /api/promotions/seller`
    * **Микросервис:** `Promotion Service`
    * **Описание:** Создает новую акцию или промокод.

* **Настройки магазина (Shop Settings):**

    * `GET /api/sellers/me/settings`
    * **Микросервис:** `Seller Service`
    * **Описание:** Получает настройки магазина продавца.
    * `PUT /api/sellers/me/settings`
    * **Микросервис:** `Seller Service`
    * **Описание:** Обновляет настройки магазина.

* **Сотрудники (Team/Staff):**

    * `GET /api/sellers/me/staff`
    * **Микросервис:** `Seller Service` (управление сотрудниками)
    * **Описание:** Получает список сотрудников продавца.
    * `POST /api/sellers/me/staff/invite`
    * **Микросервис:** `Seller Service`
    * **Описание:** Отправляет приглашение новому сотруднику.
    * `PUT /api/sellers/me/staff/{staffId}/roles`
    * **Микросервис:** `Seller Service`
    * **Описание:** Обновляет роли и права доступа сотрудника.

-----

#### **3. Панель Администрирования (Admin Panel)**

* **Административный Дашборд (Admin Dashboard):**

    * `GET /api/admin/dashboard-stats`
    * **Микросервис:** `Admin Service` / `Analytics Service` (агрегация)
    * **Описание:** Общие статистические данные для панели администратора.
    * **Авторизация:** Требуется (JWT), роль `ADMIN`

* **Управление Пользователями (User Management):**

    * `GET /api/admin/users`
    * **Микросервис:** `User Service`
    * **Описание:** Получает список всех пользователей.
    * `GET /api/admin/users/{userId}`
    * **Микросервис:** `User Service`
    * **Описание:** Получает детали конкретного пользователя.
    * `PUT /api/admin/users/{userId}/status`
    * **Микросервис:** `User Service`
    * **Описание:** Блокирует/разблокирует пользователя.

* **Управление Продавцами (Sellers Management):**

    * `GET /api/admin/sellers`
    * **Микросервис:** `Seller Service`
    * **Описание:** Получает список всех продавцов.
    * `PUT /api/admin/sellers/{sellerId}/status`
    * **Микросервис:** `Seller Service`
    * **Описание:** Изменяет статус продавца (активен, заблокирован, на модерации).
    * `POST /api/admin/sellers/{sellerId}/approve`
    * **Микросервис:** `Seller Service`
    * **Описание:** Одобряет заявку на регистрацию продавца.

* **Управление Товарами (Product Management):**

    * `GET /api/admin/products`
    * **Микросервис:** `Product Service`
    * **Описание:** Получает список всех товаров на платформе.
    * `PUT /api/admin/products/{productId}/moderate`
    * **Микросервис:** `Product Service`
    * **Описание:** Изменяет статус модерации товара.
    * `DELETE /api/admin/products/{productId}`
    * **Микросервис:** `Product Service`
    * **Описание:** Удаляет товар.

* **Управление Заказами (Order Management):**

    * `GET /api/admin/orders`
    * **Микросервис:** `Order Service`
    * **Описание:** Получает список всех заказов в системе.
    * `PUT /api/admin/orders/{orderId}/status`
    * **Микросервис:** `Order Service`
    * **Описание:** Обновляет статус любого заказа.

* **Отзывы (Reviews Moderation):**

    * `GET /api/admin/reviews/pending`
    * **Микросервис:** `Review Service`
    * **Описание:** Получает список отзывов, ожидающих модерации.
    * `PUT /api/admin/reviews/{reviewId}/status`
    * **Микросервис:** `Review Service`
    * **Описание:** Утверждает, отклоняет или скрывает отзыв.

* **Финансы (Finance Management):**

    * `GET /api/admin/finance/overview`
    * **Микросервис:** `Finance Service`
    * **Описание:** Сводная финансовая информация платформы.
    * `POST /api/admin/finance/payouts/{payoutId}/approve`
    * **Микросервис:** `Finance Service`
    * **Описание:** Одобряет выплату продавцу.

* **Модерация (General Moderation Queue):**

    * `GET /api/admin/moderation/queue`
    * **Микросервис:** `Moderation Service`
    * **Описание:** Получает список всех элементов, требующих модерации (жалобы, проблемные объекты).
    * `POST /api/admin/moderation/{itemId}/action`
    * **Микросервис:** `Moderation Service`
    * **Описание:** Выполняет модерационное действие над элементом.

* **Поддержка (Support/Tickets):**

    * `GET /api/admin/support/tickets`
    * **Микросервис:** `Support Service`
    * **Описание:** Получает список всех обращений пользователей.
    * `POST /api/admin/support/tickets/{ticketId}/message`
    * **Микросервис:** `Support Service`
    * **Описание:** Отправляет сообщение в чат поддержки.

* **Контент (Content/Pages):**

    * `GET /api/admin/content/pages`
    * **Микросервис:** `Content Service`
    * **Описание:** Получает список статических страниц.
    * `POST /api/admin/content/banners`
    * **Микросервис:** `Content Service`
    * **Описание:** Создает новый баннер для главной страницы.

* **Логи/Активность (Logs/Activity):**

    * `GET /api/admin/logs/activity`
    * **Микросервис:** `Logging Service` / `Admin Service`
    * **Описание:** Получает логи действий администраторов и системные события.

* **Настройки платформы (Settings):**

    * `GET /api/admin/settings`
    * **Микросервис:** `Admin Service` / `Config Service`
    * **Описание:** Получает текущие глобальные настройки платформы.
    * `PUT /api/admin/settings`
    * **Микросервис:** `Admin Service` / `Config Service`
    * **Описание:** Обновляет глобальные настройки платформы.

-----

#### **4. WMS (Warehouse Management System) как отдельный поддомен**

* **Главная / Дашборд (Dashboard):**

    * `GET /api/wms/dashboard-stats`
    * **Микросервис:** `WMS Service`
    * **Описание:** Возвращает ключевые показатели и статистику по складу (остатки, поступления, отгрузки, задания).
    * **Авторизация:** Требуется (JWT), роль `WMS_OPERATOR`

* **Приёмка товара (Inbound/Receiving):**

    * `GET /api/wms/inbound-shipments`
    * **Микросервис:** `WMS Service`
    * **Описание:** Получает список всех запланированных и текущих входящих поставок.
    * `POST /api/wms/inbound-shipments/{shipmentId}/receive`
    * **Микросервис:** `WMS Service`
    * **Описание:** Регистрирует приемку товаров по конкретной поставке (по сканированию).
    * **Тело запроса:** `{ "sku": "ABC-001", "quantity": 10, "location": "A-01-01" }`

* **Хранение (Storage/Inventory):**

    * `GET /api/wms/inventory`
    * **Микросервис:** `WMS Service`
    * **Описание:** Получает текущие остатки товаров на складе, с возможностью фильтрации по SKU, ячейкам.
    * `GET /api/wms/locations`
    * **Микросервис:** `WMS Service`
    * **Описание:** Получает информацию о складских ячейках.

* **Сборка/Комплектация (Picking/Packing):**

    * `GET /api/wms/picking-tasks`
    * **Микросервис:** `WMS Service`
    * **Описание:** Получает список заданий на отбор (picking tasks) для комплектовщиков.
    * `POST /api/wms/picking-tasks/{taskId}/complete-item`
    * **Микросервис:** `WMS Service`
    * **Описание:** Фиксирует отбор одного или нескольких товаров по заданию.
    * `POST /api/wms/picking-tasks/{taskId}/complete`
    * **Микросервис:** `WMS Service`
    * **Описание:** Завершает задание на отбор.
    * `POST /api/wms/orders/{orderId}/pack`
    * **Микросервис:** `WMS Service`
    * **Описание:** Регистрирует упаковку заказа.

* **Отгрузка (Outbound/Shipping):**

    * `GET /api/wms/outbound-shipments`
    * **Микросервис:** `WMS Service`
    * **Описание:** Получает список всех готовых к отгрузке и отгруженных заказов.
    * `POST /api/wms/outbound-shipments/{shipmentId}/dispatch`
    * **Микросервис:** `WMS Service`
    * **Описание:** Регистрирует фактическую отгрузку заказа курьеру.

* **Инвентаризация (Stocktaking/Audit):**

    * `GET /api/wms/stocktakes`
    * **Микросервис:** `WMS Service`
    * **Описание:** Получает список запланированных и завершенных инвентаризаций.
    * `POST /api/wms/stocktakes`
    * **Микросервис:** `WMS Service`
    * **Описание:** Создает новое задание на инвентаризацию.
    * `PUT /api/wms/stocktakes/{stocktakeId}/adjust`
    * **Микросервис:** `WMS Service`
    * **Описание:** Вносит корректировки по результатам инвентаризации.

* **Перемещения (Transfers):**

    * `GET /api/wms/transfers`
    * **Микросервис:** `WMS Service`
    * **Описание:** Получает список всех внутренних (между ячейками) и межскладских перемещений.
    * `POST /api/wms/transfers`
    * **Микросервис:** `WMS Service`
    * **Описание:** Создает новую заявку на перемещение.

* **Документы (Documents):**

    * `GET /api/wms/documents`
    * **Микросервис:** `WMS Service` / `Document Service`
    * **Описание:** Получает список генерируемых документов (накладные, акты).
    * `GET /api/wms/documents/{documentId}/pdf`
    * **Микросервис:** `WMS Service` / `Document Service`
    * **Описание:** Возвращает PDF-файл документа.

* **Логи действий (Logs):**

    * `GET /api/wms/logs/activity`
    * **Микросервис:** `Logging Service` / `WMS Service`
    * **Описание:** Получает логи действий операторов WMS.

* **Настройки (Settings):**

    * `GET /api/wms/settings`
    * **Микросервис:** `WMS Service`
    * **Описание:** Получает настройки WMS (например, информация о складе, параметры рабочих зон).
    * `PUT /api/wms/settings`
    * **Микросервис:** `WMS Service`
    * **Описание:** Обновляет настройки WMS.
    * `GET /api/wms/users`
    * **Микросервис:** `User Service` (для WMS-пользователей)
    * **Описание:** Получает список пользователей WMS.
    * `PUT /api/wms/users/{userId}/roles`
    * **Микросервис:** `User Service`
    * **Описание:** Управляет ролями и правами доступа пользователей WMS.

-----