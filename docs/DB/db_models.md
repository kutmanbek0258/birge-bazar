Для каждой сущности будут указаны ключевые атрибуты, их типы данных, а также связи с другими сущностями (Primary Key - PK, Foreign Key - FK). Большинство сущностей наследуют аудиторские поля от `AuditableCustom`.

### **Аудиторские поля (`AuditableCustom`)**
* `created_by` (String) - Кем создано
* `created_date` (Timestamp) - Дата создания
* `last_modified_by` (String) - Кем последнее изменение
* `last_modified_date` (Timestamp) - Дата последнего изменения

---

### **Сущности Баз Данных по Микросервисам (Database-per-Service)**

---

#### **1. User Service (Сервис Пользователей)**
*Управляет профилями пользователей, их адресами и списками избранного.*

* **Сущность: `User` (Пользователь)**
    * `user_id` (PK, UUID/BIGINT)
    * `email` (String, UNIQUE) - Электронная почта
    * `phone_number` (String, UNIQUE, nullable) - Номер телефона
    * `first_name` (String) - Имя
    * `last_name` (String) - Фамилия
    * `date_of_birth` (Date, nullable) - Дата рождения
    * `gender` (ENUM: 'MALE', 'FEMALE', 'OTHER', nullable) - Пол
    * `avatar_url` (String, nullable) - Ссылка на аватар
    * `status` (ENUM: 'ACTIVE', 'BLOCKED', 'PENDING_VERIFICATION')
    * `last_activity_at` (Timestamp)
    * *Наследует аудиторские поля*

* **Сущность: `UserAddress` (Адрес пользователя)**
    * `address_id` (PK, UUID/BIGINT)
    * `user_id` (FK, BIGINT) - Ссылка на `User.user_id`
    * `label` (String) - Метка адреса (например, "Домашний", "Рабочий")
    * `city` (String)
    * `street` (String)
    * `house_number` (String)
    * `apartment_number` (String, nullable)
    * `zip_code` (String, nullable)
    * `is_default` (Boolean) - Адрес по умолчанию
    * `latitude` (Decimal, nullable) - Широта
    * `longitude` (Decimal, nullable) - Долгота
    * *Наследует аудиторские поля*

* **Сущность: `FavoriteProduct` (Избранный товар)**
    * `favorite_id` (PK, UUID/BIGINT)
    * `user_id` (FK, BIGINT) - Ссылка на `User.user_id`
    * `product_id` (BIGINT) - ID товара (внешний ключ к `Product Service.Product.product_id`) - **Не FK на уровне БД**
    * *Наследует аудиторские поля*

---

#### **2. Auth Service (Сервис Аутентификации)**
*Отвечает за аутентификацию, авторизацию, управление сессиями и 2FA. Хранит чувствительные данные для аутентификации.*

* **Сущность: `UserCredentials` (Учетные данные пользователя)**
    * `user_id` (PK, BIGINT) - Ссылка на `User.user_id` (User Service)
    * `password_hash` (String) - Хеш пароля
    * `email_verified` (Boolean)
    * `phone_verified` (Boolean)
    * *Наследует аудиторские поля*

* **Сущность: `UserSession` (Сессия пользователя)**
    * `session_id` (PK, UUID/BIGINT)
    * `user_id` (FK, BIGINT) - Ссылка на `User.user_id` (User Service)
    * `jwt_token` (String) - JWT токен (может храниться для отзыва)
    * `refresh_token` (String, UNIQUE) - Рефреш токен
    * `device_info` (String, nullable) - Информация об устройстве (User-Agent)
    * `ip_address` (String) - IP-адрес входа
    * `expires_at` (Timestamp) - Время истечения сессии
    * `is_active` (Boolean)
    * *Наследует аудиторские поля*

* **Сущность: `TwoFactorAuthSetting` (Настройка 2FA)**
    * `user_id` (PK, BIGINT) - Ссылка на `User.user_id` (User Service)
    * `is_enabled` (Boolean)
    * `method` (ENUM: 'TOTP', 'SMS', 'EMAIL', nullable) - Предпочитаемый метод 2FA
    * `totp_secret` (String, nullable) - Секретный ключ для TOTP
    * `sms_code_last_sent_at` (Timestamp, nullable)
    * `email_code_last_sent_at` (Timestamp, nullable)
    * *Наследует аудиторские поля*

* **Сущность: `LoginHistory` (История входов)**
    * `entry_id` (PK, UUID/BIGINT)
    * `user_id` (FK, BIGINT) - Ссылка на `User.user_id` (User Service)
    * `ip_address` (String)
    * `device_info` (String, nullable)
    * `status` (ENUM: 'SUCCESS', 'FAILED')
    * `created_date` (Timestamp)

---

#### **3. Product Service (Сервис Товаров)**
*Управляет каталогом товаров, их характеристиками, изображениями и категориями.*

* **Сущность: `Product` (Товар)**
    * `product_id` (PK, UUID/BIGINT)
    * `seller_id` (BIGINT) - ID продавца (внешний ключ к `Seller Service.Seller.seller_id`) - **Не FK на уровне БД**
    * `name` (String) - Название товара
    * `description` (Text) - Полное описание
    * `short_description` (String, nullable) - Краткое описание
    * `base_price` (Decimal) - Базовая цена
    * `category_id` (FK, BIGINT) - Ссылка на `Category.category_id`
    * `brand_id` (FK, BIGINT, nullable) - Ссылка на `Brand.brand_id`
    * `status` (ENUM: 'ACTIVE', 'DRAFT', 'PENDING_MODERATION', 'REJECTED', 'ARCHIVED')
    * `main_image_url` (String)
    * `moderated_at` (Timestamp, nullable)
    * *Наследует аудиторские поля*

* **Сущность: `ProductImage` (Изображение товара)**
    * `image_id` (PK, UUID/BIGINT)
    * `product_id` (FK, BIGINT) - Ссылка на `Product.product_id`
    * `url` (String) - URL изображения
    * `is_main` (Boolean) - Является ли основным изображением
    * `order_index` (Integer) - Порядок отображения
    * *Наследует аудиторские поля*

* **Сущность: `ProductVariant` (Вариант товара)**
    * `variant_id` (PK, UUID/BIGINT)
    * `product_id` (FK, BIGINT) - Ссылка на `Product.product_id`
    * `sku_code` (String, UNIQUE) - Уникальный SKU для конкретного варианта
    * `attributes_json` (JSONB) - Характеристики варианта (например, `{"Цвет": "синий", "Размер": "L"}`)
    * `price_modifier` (Decimal) - Добавка/скидка к базовой цене
    * `stock_quantity_fbs` (Integer) - Доступное количество для FBS (если продавец сам управляет остатками)
    * `image_url` (String, nullable) - Изображение для конкретного варианта
    * `is_active` (Boolean)
    * *Наследует аудиторские поля*

* **Сущность: `Category` (Категория)**
    * `category_id` (PK, UUID/BIGINT)
    * `name` (String) - Название категории
    * `parent_id` (FK, BIGINT, nullable) - Ссылка на `Category.category_id` (для вложенности)
    * `slug` (String, UNIQUE) - URL-идентификатор
    * `icon_url` (String, nullable)
    * *Наследует аудиторские поля*

* **Сущность: `Brand` (Бренд)**
    * `brand_id` (PK, UUID/BIGINT)
    * `name` (String, UNIQUE) - Название бренда
    * `logo_url` (String, nullable) - Ссылка на логотип
    * `description` (Text, nullable)
    * *Наследует аудиторские поля*

---

#### **4. Order Service (Сервис Заказов)**
*Управляет жизненным циклом заказов, их статусами и деталями.*

* **Сущность: `Order` (Заказ)**
    * `order_id` (PK, UUID/BIGINT)
    * `user_id` (BIGINT) - ID покупателя (внешний ключ к `User Service.User.user_id`)
    * `seller_id` (BIGINT) - ID продавца (внешний ключ к `Seller Service.Seller.seller_id`)
    * `status` (ENUM: 'PENDING', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED', 'REFUNDED')
    * `total_amount` (Decimal) - Общая сумма заказа
    * `discount_amount` (Decimal) - Сумма скидки
    * `shipping_cost` (Decimal) - Стоимость доставки
    * `shipping_address_json` (JSONB) - Адрес доставки (копия данных из User Service для истории)
    * `payment_method_id` (BIGINT, nullable) - ID способа оплаты (внешний ключ к `Payment Service.PaymentMethod.method_id`) - **Не FK на уровне БД**
    * `payment_status` (ENUM: 'PENDING', 'PAID', 'FAILED', 'REFUNDED', 'AUTHORIZED')
    * `shipping_method_id` (BIGINT) - ID способа доставки (внешний ключ к `Logistics Service.ShippingMethod.method_id`) - **Не FK на уровне БД**
    * `tracking_number` (String, nullable) - Номер отслеживания (от Logistics Service)
    * `order_type` (ENUM: 'FBO', 'FBS_SELLER_DELIVERY', 'FBS_MARKETPLACE_DELIVERY')
    * `expected_delivery_date` (Date, nullable)
    * `delivered_at` (Timestamp, nullable)
    * *Наследует аудиторские поля*

* **Сущность: `OrderItem` (Позиция заказа)**
    * `order_item_id` (PK, UUID/BIGINT)
    * `order_id` (FK, BIGINT) - Ссылка на `Order.order_id`
    * `product_id` (BIGINT) - ID товара (внешний ключ к `Product Service.Product.product_id`)
    * `product_variant_id` (BIGINT, nullable) - ID варианта товара (внешний ключ к `Product Service.ProductVariant.variant_id`)
    * `sku_code` (String) - SKU товара (для быстрого доступа)
    * `product_name` (String) - Дублирование названия товара на момент заказа
    * `quantity` (Integer) - Количество
    * `unit_price` (Decimal) - Цена за единицу на момент заказа
    * `total_item_price` (Decimal) - `quantity * unit_price`
    * *Наследует аудиторские поля*

* **Сущность: `OrderStatusHistory` (История статусов заказа)**
    * `history_id` (PK, UUID/BIGINT)
    * `order_id` (FK, BIGINT) - Ссылка на `Order.order_id`
    * `status` (ENUM: 'PENDING', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED', 'REFUNDED')
    * `changed_by_user_id` (BIGINT, nullable) - ID пользователя/админа, изменившего статус
    * `created_date` (Timestamp)

---

#### **5. Review Service (Сервис Отзывов)**
*Управляет отзывами, рейтингами и комментариями к товарам и продавцам.*

* **Сущность: `Review` (Отзыв)**
    * `review_id` (PK, UUID/BIGINT)
    * `user_id` (BIGINT) - ID пользователя (внешний ключ к `User Service.User.user_id`)
    * `product_id` (BIGINT) - ID товара (внешний ключ к `Product Service.Product.product_id`)
    * `order_id` (BIGINT, nullable) - ID заказа (внешний ключ к `Order Service.Order.order_id`)
    * `rating` (Integer) - Оценка (от 1 до 5)
    * `comment` (Text, nullable) - Текст отзыва
    * `is_anonymous` (Boolean) - Анонимный отзыв
    * `status` (ENUM: 'PENDING_MODERATION', 'APPROVED', 'REJECTED', 'HIDDEN')
    * *Наследует аудиторские поля*

* **Сущность: `ReviewImage` (Изображение отзыва)**
    * `image_id` (PK, UUID/BIGINT)
    * `review_id` (FK, BIGINT) - Ссылка на `Review.review_id`
    * `url` (String) - URL изображения
    * *Наследует аудиторские поля*

* **Сущность: `SellerReply` (Ответ продавца)**
    * `reply_id` (PK, UUID/BIGINT)
    * `review_id` (FK, BIGINT) - Ссылка на `Review.review_id`
    * `seller_id` (BIGINT) - ID продавца (внешний ключ к `Seller Service.Seller.seller_id`)
    * `reply_text` (Text)
    * *Наследует аудиторские поля*

---

#### **6. Payment Service (Сервис Платежей)**
*Обрабатывает все финансовые транзакции и управляет сохраненными способами оплаты.*

* **Сущность: `PaymentTransaction` (Платежная транзакция)**
    * `transaction_id` (PK, UUID/BIGINT)
    * `order_id` (FK, BIGINT, nullable) - Ссылка на `Order Service.Order.order_id`
    * `user_id` (BIGINT) - ID пользователя (внешний ключ к `User Service.User.user_id`)
    * `amount` (Decimal)
    * `currency` (String)
    * `status` (ENUM: 'PENDING', 'SUCCESS', 'FAILED', 'REFUNDED', 'AUTHORIZED')
    * `gateway_transaction_id` (String, UNIQUE) - ID транзакции в платежной системе
    * `payment_method_type` (ENUM: 'CARD', 'CASH_ON_DELIVERY', 'E_WALLET')
    * `error_message` (String, nullable)
    * `processed_at` (Timestamp)
    * *Наследует аудиторские поля*

* **Сущность: `SavedPaymentMethod` (Сохраненный способ оплаты)**
    * `method_id` (PK, UUID/BIGINT)
    * `user_id` (FK, BIGINT) - Ссылка на `User Service.User.user_id`
    * `type` (ENUM: 'CARD')
    * `token` (String) - Токен карты от платежного провайдера
    * `last_four_digits` (String) - Последние 4 цифры карты
    * `card_brand` (String, nullable) - Бренд карты (Visa, MasterCard)
    * `expiry_month` (Integer, nullable)
    * `expiry_year` (Integer, nullable)
    * `is_default` (Boolean)
    * *Наследует аудиторские поля*

* **Сущность: `PayoutRequest` (Запрос на выплату продавцу)**
    * `payout_id` (PK, UUID/BIGINT)
    * `seller_id` (BIGINT) - ID продавца (внешний ключ к `Seller Service.Seller.seller_id`)
    * `amount` (Decimal)
    * `status` (ENUM: 'PENDING', 'APPROVED', 'PAID', 'REJECTED')
    * `processed_at` (Timestamp, nullable)
    * `transaction_id` (String, nullable) - ID транзакции выплаты в банке
    * *Наследует аудиторские поля*

---

#### **7. Notification Service (Сервис Уведомлений)**
*Отправляет и управляет всеми уведомлениями пользователям и продавцам.*

* **Сущность: `Notification` (Уведомление)**
    * `notification_id` (PK, UUID/BIGINT)
    * `user_id` (FK, BIGINT) - Ссылка на `User Service.User.user_id`
    * `type` (ENUM: 'ORDER_STATUS', 'PROMOTION', 'MESSAGE', 'REVIEW_REMINDER', 'WMS_ALERT')
    * `message_text` (Text) - Текст уведомления
    * `link_url` (String, nullable) - URL для перехода по уведомлению
    * `is_read` (Boolean)
    * `sent_at` (Timestamp)
    * *Наследует аудиторские поля*

* **Сущность: `NotificationSetting` (Настройки уведомлений)**
    * `user_id` (PK, BIGINT) - Ссылка на `User Service.User.user_id`
    * `email_enabled` (Boolean)
    * `sms_enabled` (Boolean)
    * `push_enabled` (Boolean)
    * `order_status_updates` (Boolean)
    * `promotion_updates` (Boolean)
    * `message_notifications` (Boolean)
    * `review_reminders` (Boolean)
    * `wms_alerts` (Boolean) - Для продавцов/WMS-операторов
    * *Наследует аудиторские поля*

---

#### **8. Logistics Service (Сервис Логистики)**
*Описывает методы доставки, пункты выдачи и управляет логистическими партнерами.*

* **Сущность: `ShippingMethod` (Способ доставки)**
    * `method_id` (PK, UUID/BIGINT)
    * `name` (String) - Название (например, "Курьерская доставка", "Самовывоз")
    * `description` (String, nullable)
    * `base_cost` (Decimal)
    * `min_delivery_days` (Integer)
    * `max_delivery_days` (Integer)
    * `is_active` (Boolean)
    * `carrier_id` (FK, BIGINT, nullable) - Ссылка на `ShippingCarrier.carrier_id` (если внешняя служба)
    * `is_marketplace_delivery` (Boolean) - Доставка силами маркетплейса
    * *Наследует аудиторские поля*

* **Сущность: `ShippingCarrier` (Служба доставки)**
    * `carrier_id` (PK, UUID/BIGINT)
    * `name` (String, UNIQUE) - Название перевозчика (например, "Почта Кыргызстана", "DHL")
    * `tracking_url_pattern` (String, nullable) - Шаблон URL для отслеживания посылок
    * `api_key_encrypted` (String, nullable) - Зашифрованный API ключ для интеграции
    * *Наследует аудиторские поля*

* **Сущность: `PickupPoint` (Пункт выдачи)**
    * `point_id` (PK, UUID/BIGINT)
    * `name` (String)
    * `address` (String)
    * `city` (String)
    * `latitude` (Decimal)
    * `longitude` (Decimal)
    * `working_hours_json` (JSONB) - Расписание работы (например, `{"Mon": "9:00-18:00"}`)
    * `is_active` (Boolean)
    * *Наследует аудиторские поля*

---

#### **9. WMS Service (Сервис Управления Складом)**
*Управляет физическим перемещением и хранением товаров на складах маркетплейса (для FBO и FBS с доставкой маркетплейса).*

* **Сущность: `Warehouse` (Склад)**
    * `warehouse_id` (PK, UUID/BIGINT)
    * `name` (String) - Название склада
    * `address` (String)
    * `capacity_sq_m` (Decimal, nullable) - Площадь склада в кв. метрах
    * `is_active` (Boolean)
    * *Наследует аудиторские поля*

* **Сущность: `Location` (Ячейка / Место хранения)**
    * `location_id` (PK, UUID/BIGINT)
    * `warehouse_id` (FK, BIGINT) - Ссылка на `Warehouse.warehouse_id`
    * `code` (String) - Код ячейки (например, "A-01-01")
    * `type` (ENUM: 'SHELF', 'PALLET', 'FLOOR', 'BIN')
    * `max_capacity_units` (Integer, nullable) - Максимальная емкость ячейки
    * `is_active` (Boolean)
    * *Наследует аудиторские поля*

* **Сущность: `InventoryItem` (Складская позиция / Запас)**
    * `inventory_item_id` (PK, UUID/BIGINT)
    * `sku_code` (String) - SKU товара (внешний ключ к `Product Service.ProductVariant.sku_code`) - **Не FK на уровне БД**
    * `warehouse_id` (FK, BIGINT) - Ссылка на `Warehouse.warehouse_id`
    * `location_id` (FK, BIGINT) - Ссылка на `Location.location_id`
    * `quantity` (Integer) - Количество товара в данной ячейке
    * `batch_number` (String, nullable) - Номер партии
    * `expiration_date` (Date, nullable)
    * `seller_id` (BIGINT) - ID продавца (внешний ключ к `Seller Service.Seller.seller_id`)
    * `status` (ENUM: 'AVAILABLE', 'RESERVED', 'QUARANTINE', 'DAMAGED')
    * *Наследует аудиторские поля*

* **Сущность: `StockMovement` (Движение запаса)**
    * `movement_id` (PK, UUID/BIGINT)
    * `inventory_item_id` (FK, BIGINT, nullable) - Ссылка на `InventoryItem.inventory_item_id` (если движение конкретной единицы)
    * `sku_code` (String) - SKU товара
    * `quantity` (Integer) - Измененное количество (может быть отрицательным)
    * `from_location_id` (FK, BIGINT, nullable) - Ссылка на `Location.location_id`
    * `to_location_id` (FK, BIGINT, nullable) - Ссылка на `Location.location_id`
    * `movement_type` (ENUM: 'RECEIVE', 'PICK', 'SHIP', 'TRANSFER', 'ADJUSTMENT_IN', 'ADJUSTMENT_OUT')
    * `reference_document_id` (BIGINT, nullable) - ID связанного документа (Inbound, Order, Stocktake)
    * `reference_document_type` (String, nullable) - Тип связанного документа
    * `user_id` (BIGINT) - ID пользователя, инициировавшего движение (внешний ключ к `User Service.User.user_id`)
    * `moved_at` (Timestamp)
    * *Наследует аудиторские поля*

* **Сущность: `InboundShipment` (Входящая поставка)**
    * `inbound_id` (PK, UUID/BIGINT)
    * `seller_id` (BIGINT) - ID продавца (внешний ключ к `Seller Service.Seller.seller_id`)
    * `warehouse_id` (FK, BIGINT) - Ссылка на `Warehouse.warehouse_id`
    * `status` (ENUM: 'PLANNED', 'IN_TRANSIT', 'RECEIVED_PARTIAL', 'RECEIVED_COMPLETED', 'CANCELLED')
    * `expected_arrival_date` (Date)
    * `actual_arrival_date` (Timestamp, nullable)
    * `notes` (Text, nullable)
    * *Наследует аудиторские поля*

* **Сущность: `InboundItem` (Позиция входящей поставки)**
    * `inbound_item_id` (PK, UUID/BIGINT)
    * `inbound_id` (FK, BIGINT) - Ссылка на `InboundShipment.inbound_id`
    * `sku_code` (String) - SKU ожидаемого товара
    * `expected_quantity` (Integer)
    * `received_quantity` (Integer) - Фактически принятое количество
    * *Наследует аудиторские поля*

* **Сущность: `PickingTask` (Задание на отбор)**
    * `task_id` (PK, UUID/BIGINT)
    * `order_id` (BIGINT) - ID заказа (внешний ключ к `Order Service.Order.order_id`)
    * `warehouse_id` (FK, BIGINT) - Ссылка на `Warehouse.warehouse_id`
    * `picker_user_id` (BIGINT, nullable) - ID сотрудника склада (внешний ключ к `User Service.User.user_id`)
    * `status` (ENUM: 'NEW', 'ASSIGNED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED')
    * `assigned_at` (Timestamp, nullable)
    * `completed_at` (Timestamp, nullable)
    * *Наследует аудиторские поля*

* **Сущность: `PickingTaskItem` (Позиция задания на отбор)**
    * `task_item_id` (PK, UUID/BIGINT)
    * `task_id` (FK, BIGINT) - Ссылка на `PickingTask.task_id`
    * `sku_code` (String) - SKU товара
    * `quantity_to_pick` (Integer)
    * `picked_quantity` (Integer)
    * `source_location_id` (FK, BIGINT, nullable) - Ссылка на `Location.location_id` (рекомендуемое место)
    * *Наследует аудиторские поля*

* **Сущность: `OutboundShipment` (Исходящая отгрузка)**
    * `outbound_id` (PK, UUID/BIGINT)
    * `order_id` (BIGINT) - ID заказа (внешний ключ к `Order Service.Order.order_id`)
    * `warehouse_id` (FK, BIGINT) - Ссылка на `Warehouse.warehouse_id`
    * `status` (ENUM: 'PENDING_PICKING', 'READY_FOR_DISPATCH', 'DISPATCHED', 'DELIVERED')
    * `carrier_id` (BIGINT, nullable) - ID перевозчика (внешний ключ к `Logistics Service.ShippingCarrier.carrier_id`)
    * `tracking_number` (String, nullable)
    * `dispatch_at` (Timestamp, nullable)
    * *Наследует аудиторские поля*

* **Сущность: `Stocktake` (Инвентаризация)**
    * `stocktake_id` (PK, UUID/BIGINT)
    * `warehouse_id` (FK, BIGINT) - Ссылка на `Warehouse.warehouse_id`
    * `type` (ENUM: 'FULL', 'CYCLE_COUNT')
    * `status` (ENUM: 'PLANNED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED')
    * `start_date` (Date)
    * `end_date` (Date, nullable)
    * `initiated_by_user_id` (BIGINT) - ID пользователя (внешний ключ к `User Service.User.user_id`)
    * *Наследует аудиторские поля*

* **Сущность: `StocktakeItem` (Позиция инвентаризации)**
    * `stocktake_item_id` (PK, UUID/BIGINT)
    * `stocktake_id` (FK, BIGINT) - Ссылка на `Stocktake.stocktake_id`
    * `sku_code` (String) - SKU товара
    * `location_id` (FK, BIGINT, nullable) - Ссылка на `Location.location_id`
    * `system_quantity` (Integer) - Количество по системе
    * `counted_quantity` (Integer) - Фактически посчитанное количество
    * `discrepancy` (Integer) - Разница (`counted_quantity - system_quantity`)
    * *Наследует аудиторские поля*

* **Сущность: `Transfer` (Перемещение)**
    * `transfer_id` (PK, UUID/BIGINT)
    * `sku_code` (String) - SKU товара
    * `quantity` (Integer)
    * `from_location_id` (FK, BIGINT) - Ссылка на `Location.location_id` (откуда)
    * `to_location_id` (FK, BIGINT) - Ссылка на `Location.location_id` (куда)
    * `status` (ENUM: 'PENDING', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED')
    * `initiated_by_user_id` (BIGINT) - ID пользователя (внешний ключ к `User Service.User.user_id`)
    * `completed_at` (Timestamp, nullable)
    * *Наследует аудиторские поля*

---

#### **10. Admin Service (Сервис Администрирования)**
*Управляет функциями администрирования платформы, модерацией и глобальными настройками.*

* **Сущность: `AdminUser` (Администратор)**
    * `admin_id` (PK, UUID/BIGINT)
    * `user_id` (FK, BIGINT) - Ссылка на `User Service.User.user_id` (если админ это обычный пользователь с расширенными правами)
    * `username` (String, UNIQUE) - Логин администратора
    * `password_hash` (String) - Хеш пароля
    * `roles` (Array/JSONB) - Роли администратора (например, 'SUPER_ADMIN', 'MODERATOR', 'FINANCE_MANAGER')
    * `is_active` (Boolean)
    * *Наследует аудиторские поля*

* **Сущность: `PlatformSetting` (Настройка платформы)**
    * `setting_id` (PK, UUID/BIGINT)
    * `setting_key` (String, UNIQUE) - Ключ настройки (например, "commission_rate", "min_payout_amount")
    * `setting_value` (Text) - Значение настройки
    * `value_type` (ENUM: 'STRING', 'NUMBER', 'BOOLEAN', 'JSON')
    * `description` (String, nullable) - Описание настройки
    * *Наследует аудиторские поля*

* **Сущность: `ModerationItem` (Элемент модерации)**
    * `moderation_item_id` (PK, UUID/BIGINT)
    * `item_type` (ENUM: 'PRODUCT', 'REVIEW', 'SELLER', 'USER', 'COMPLAINT')
    * `item_id` (BIGINT) - ID связанного элемента (например, `product_id`, `review_id`)
    * `reason` (Text) - Причина модерации/жалобы
    * `status` (ENUM: 'PENDING', 'IN_REVIEW', 'APPROVED', 'REJECTED', 'ACTION_TAKEN')
    * `assigned_to_admin_id` (FK, BIGINT, nullable) - Ссылка на `AdminUser.admin_id`
    * `resolved_at` (Timestamp, nullable)
    * *Наследует аудиторские поля*

* **Сущность: `AdminActivityLog` (Журнал активности администратора)**
    * `log_id` (PK, UUID/BIGINT)
    * `admin_user_id` (FK, BIGINT) - Ссылка на `AdminUser.admin_id`
    * `action` (String) - Описание действия (например, "Изменил статус товара", "Заблокировал пользователя")
    * `resource_type` (String) - Тип ресурса (например, "PRODUCT", "USER", "ORDER")
    * `resource_id` (BIGINT, nullable) - ID ресурса
    * `ip_address` (String)
    * `created_date` (Timestamp)

---

#### **11. Seller Service (Сервис Продавцов)**
*Управляет данными продавцов, их статусами, настройками магазина и сотрудниками.*

* **Сущность: `Seller` (Продавец)**
    * `seller_id` (PK, UUID/BIGINT)
    * `user_id` (FK, BIGINT, UNIQUE) - Ссылка на `User Service.User.user_id` (основной аккаунт владельца магазина)
    * `shop_name` (String, UNIQUE) - Название магазина
    * `description` (Text, nullable)
    * `logo_url` (String, nullable)
    * `status` (ENUM: 'PENDING_APPROVAL', 'ACTIVE', 'BLOCKED', 'SUSPENDED')
    * `commission_rate` (Decimal) - Комиссия маркетплейса для этого продавца (может быть переопределена)
    * `contact_email` (String)
    * `contact_phone` (String, nullable)
    * `bank_details_json` (JSONB, nullable) - Банковские реквизиты для выплат (зашифрованные)
    * `approval_date` (Timestamp, nullable)
    * *Наследует аудиторские поля*

* **Сущность: `SellerStaff` (Сотрудник продавца)**
    * `staff_id` (PK, UUID/BIGINT)
    * `seller_id` (FK, BIGINT) - Ссылка на `Seller.seller_id`
    * `user_id` (FK, BIGINT, UNIQUE) - Ссылка на `User Service.User.user_id` (аккаунт сотрудника)
    * `role` (ENUM: 'MANAGER', 'PRODUCT_EDITOR', 'ORDER_PROCESSOR', 'FINANCIER', 'MESSENGER') - Роль сотрудника в магазине
    * `is_active` (Boolean)
    * `accepted_at` (Timestamp, nullable)
    * *Наследует аудиторские поля*

---

#### **12. Promotion Service (Сервис Акций и Промокодов)**
*Управляет созданием, применением и учетом промоакций.*

* **Сущность: `Promotion` (Акция/Промокод)**
    * `promotion_id` (PK, UUID/BIGINT)
    * `name` (String)
    * `code` (String, UNIQUE, nullable) - Промокод (если акция по коду)
    * `description` (Text, nullable)
    * `discount_type` (ENUM: 'PERCENTAGE', 'FIXED_AMOUNT', 'FREE_SHIPPING')
    * `discount_value` (Decimal)
    * `start_date` (Timestamp)
    * `end_date` (Timestamp)
    * `usage_limit_total` (Integer, nullable) - Общее количество использований
    * `usage_limit_per_user` (Integer, nullable) - Ограничение на пользователя
    * `min_order_amount` (Decimal, nullable) - Минимальная сумма заказа для применения
    * `applies_to_category_ids` (Array/JSONB, nullable) - ID категорий, к которым применяется
    * `applies_to_product_ids` (Array/JSONB, nullable) - ID товаров, к которым применяется
    * `applies_to_seller_ids` (Array/JSONB, nullable) - ID продавцов, к которым применяется
    * `is_active` (Boolean)
    * *Наследует аудиторские поля*

* **Сущность: `UserPromotionUsage` (Использование акции пользователем)**
    * `usage_id` (PK, UUID/BIGINT)
    * `user_id` (FK, BIGINT) - Ссылка на `User Service.User.user_id`
    * `promotion_id` (FK, BIGINT) - Ссылка на `Promotion.promotion_id`
    * `usage_count` (Integer) - Сколько раз пользователь использовал эту акцию
    * `order_id` (BIGINT, nullable) - ID заказа, в котором была использована акция
    * *Наследует аудиторские поля*

---

#### **13. QA Service (Сервис Вопросов и Ответов)**
*Управляет вопросами пользователей и ответами на странице товара.*

* **Сущность: `Question` (Вопрос)**
    * `question_id` (PK, UUID/BIGINT)
    * `product_id` (BIGINT) - ID товара (внешний ключ к `Product Service.Product.product_id`)
    * `user_id` (FK, BIGINT) - Ссылка на `User Service.User.user_id` (кто задал вопрос)
    * `text` (Text) - Текст вопроса
    * `status` (ENUM: 'PENDING_ANSWER', 'ANSWERED', 'HIDDEN')
    * *Наследует аудиторские поля*

* **Сущность: `Answer` (Ответ)**
    * `answer_id` (PK, UUID/BIGINT)
    * `question_id` (FK, BIGINT) - Ссылка на `Question.question_id`
    * `responder_id` (BIGINT) - ID ответившего (может быть `User.user_id`, `Seller.seller_id`, `AdminUser.admin_id`)
    * `responder_type` (ENUM: 'USER', 'SELLER', 'ADMIN')
    * `text` (Text) - Текст ответа
    * *Наследует аудиторские поля*

---

#### **14. Messaging Service (Сервис Сообщений/Чата)**
*Управляет перепиской между покупателями, продавцами и поддержкой.*

* **Сущность: `ChatConversation` (Чат/Диалог)**
    * `conversation_id` (PK, UUID/BIGINT)
    * `participant1_id` (FK, BIGINT) - ID первого участника (User ID)
    * `participant1_type` (ENUM: 'USER', 'SELLER', 'ADMIN')
    * `participant2_id` (FK, BIGINT) - ID второго участника (User ID)
    * `participant2_type` (ENUM: 'USER', 'SELLER', 'ADMIN')
    * `subject` (String, nullable) - Тема чата (например, "Вопрос по заказу #123")
    * `is_active` (Boolean)
    * *Наследует аудиторские поля*

* **Сущность: `ChatMessage` (Сообщение в чате)**
    * `message_id` (PK, UUID/BIGINT)
    * `conversation_id` (FK, BIGINT) - Ссылка на `ChatConversation.conversation_id`
    * `sender_id` (BIGINT) - ID отправителя (User ID)
    * `sender_type` (ENUM: 'USER', 'SELLER', 'ADMIN')
    * `message_text` (Text)
    * `is_read_by_recipient` (Boolean)
    * *Наследует аудиторские поля*

---

#### **15. Search Service (Сервис Поиска)**
*Этот сервис обычно использует нереляционные базы данных, такие как Elasticsearch, для индексации данных. Его собственная реляционная БД, если она есть, будет хранить метаданные или статистику, а не сами сущности товаров.*

* **Сущность: `SearchIndexConfig` (Конфигурация поискового индекса)**
    * `config_id` (PK, UUID/BIGINT)
    * `index_name` (String, UNIQUE)
    * `source_service` (String) - Имя микросервиса-источника данных
    * `last_indexed_at` (Timestamp)
    * `indexing_schedule` (String) - Cron-выражение для расписания индексации
    * *Наследует аудиторские поля*

* **Сущность: `SearchQueryLog` (Лог поисковых запросов)**
    * `log_id` (PK, UUID/BIGINT)
    * `query_text` (String)
    * `user_id` (BIGINT, nullable)
    * `results_count` (Integer)
    * `created_date` (Timestamp)

---

#### **16. Cart Service (Сервис Корзины)**
*Управляет временными корзинами пользователей и их содержимым.*

* **Сущность: `Cart` (Корзина)**
    * `cart_id` (PK, UUID/BIGINT)
    * `user_id` (FK, BIGINT, nullable) - Ссылка на `User Service.User.user_id`. Nullable для анонимных корзин.
    * `session_id` (String, nullable) - ID сессии для анонимных пользователей.
    * `expires_at` (Timestamp, nullable) - Время истечения срока действия корзины (для анонимных).
    * *Наследует аудиторские поля*

* **Сущность: `CartItem` (Позиция в корзине)**
    * `cart_item_id` (PK, UUID/BIGINT)
    * `cart_id` (FK, BIGINT) - Ссылка на `Cart.cart_id`
    * `product_id` (BIGINT) - ID товара (внешний ключ к `Product Service.Product.product_id`)
    * `product_variant_id` (BIGINT, nullable) - ID варианта товара (внешний ключ к `Product Service.ProductVariant.variant_id`)
    * `quantity` (Integer) - Количество товара в корзине
    * `price_at_add` (Decimal) - Цена товара на момент добавления в корзину (для истории)
    * `added_at` (Timestamp) - Время добавления позиции
    * *Наследует аудиторские поля*

---

**Важные примечания:**

* **Внешние ключи (FK):** В микросервисной архитектуре прямые FK между базами данных разных сервисов не создаются. Вместо этого, `FK` указывают на логическую связь по `ID` между сущностями разных сервисов. Целостность данных обеспечивается на уровне бизнес-логики сервисов и Eventual Consistency.
* **UUID vs. BIGINT:** Для `id` можно использовать либо `UUID` (для глобальной уникальности без координации между сервисами), либо `BIGINT` (для последовательных ID, требующих координации или генерации в рамках одного сервиса). В этом описании указаны оба варианта, выбор зависит от конкретной реализации.
* **JSONB:** Использование `JSONB` (в PostgreSQL) позволяет хранить гибкие структуры данных, что удобно для атрибутов товаров, настроек, банковских реквизитов и т.п.
* **Дублирование данных:** В некоторых случаях (например, `product_name` и `sku_code` в `OrderItem`, `shipping_address_json` в `Order`) данные дублируются для обеспечения целостности заказа на момент его создания, даже если исходный товар или адрес будут изменены/удалены. Это паттерн "дедупликации данных" в микросервисах.