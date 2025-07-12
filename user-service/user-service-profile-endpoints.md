# user-service: Эндпоинты и описания (на основе мокапов страницы и подстраниц user profile)

---

## 1. Получение профиля пользователя

**GET /profile**
- Описание: Получить информацию о текущем пользователе (имя, email, телефон, дата рождения, пол, аватар, дата регистрации).
- Ответ:
  ```json
  {
    "id": "string",
    "fullName": "string",
    "email": "string",
    "phone": "string",
    "birthDate": "string|null",
    "gender": "male|female|other|null",
    "avatarUrl": "string|null",
    "createdAt": "string (ISO8601)"
  }
  ```

---

## 2. Обновление профиля пользователя

**PUT /profile**
- Описание: Обновить личные данные пользователя (имя, телефон, дата рождения, пол, аватар).
- Тело запроса:
  ```json
  {
    "fullName": "string",
    "phone": "string",
    "birthDate": "string|null",
    "gender": "male|female|other|null",
    "avatarUrl": "string|null"
  }
  ```
- Ответ:
  ```json
  {
    "success": true
  }
  ```

---

## 3. Смена пароля

**POST /profile/change-password**
- Описание: Сменить пароль пользователя.
- Тело запроса:
  ```json
  {
    "oldPassword": "string",
    "newPassword": "string"
  }
  ```
- Ответ:
  ```json
  {
    "success": true
  }
  ```

---

## 4. История заказов пользователя

**GET /profile/orders**
- Описание: Получить список заказов пользователя (id, дата, статус, итоговая сумма, товары).
- Параметры:  
  - `limit` (int, default 20)
  - `offset` (int, default 0)
- Ответ:
  ```json
  [
    {
      "id": "string",
      "createdAt": "string (ISO8601)",
      "status": "new|paid|shipped|completed|cancelled",
      "totalAmount": "number",
      "items": [
        {
          "productId": "string",
          "name": "string",
          "qty": "number",
          "price": "number"
        }
      ]
    }
  ]
  ```

**GET /profile/orders/{orderId}**
- Описание: Получить подробную информацию по заказу.
- Ответ:
  ```json
  {
    "id": "string",
    "createdAt": "string",
    "status": "string",
    "totalAmount": "number",
    "items": [ ... ],
    "shippingAddress": { ... },
    "paymentInfo": { ... }
  }
  ```

---

## 5. Избранное (Wishlist)

**GET /profile/favorites**
- Описание: Получить список избранных товаров пользователя.
- Ответ:
  ```json
  [
    {
      "productId": "string",
      "name": "string",
      "imageUrl": "string|null"
    }
  ]
  ```

**POST /profile/favorites**
- Описание: Добавить товар в избранное.
- Тело запроса:
  ```json
  {
    "productId": "string"
  }
  ```
- Ответ:
  ```json
  {
    "success": true
  }
  ```

**DELETE /profile/favorites/{productId}**
- Описание: Удалить товар из избранного.
- Ответ:
  ```json
  {
    "success": true
  }
  ```

---

## 6. Отзывы пользователя

**GET /profile/reviews**
- Описание: Получить список отзывов пользователя.
- Ответ:
  ```json
  [
    {
      "reviewId": "string",
      "productId": "string",
      "productName": "string",
      "rating": "int",
      "text": "string",
      "createdAt": "string"
    }
  ]
  ```

**PUT /profile/reviews/{reviewId}**
- Описание: Редактировать отзыв пользователя.
- Тело запроса:
  ```json
  {
    "rating": "int",
    "text": "string"
  }
  ```
- Ответ:
  ```json
  {
    "success": true
  }
  ```

**DELETE /profile/reviews/{reviewId}**
- Описание: Удалить отзыв пользователя.
- Ответ:
  ```json
  {
    "success": true
  }
  ```

---

## 7. Адреса доставки

**GET /profile/addresses**
- Описание: Получить список адресов пользователя.
- Ответ:
  ```json
  [
    {
      "id": "string",
      "city": "string",
      "street": "string",
      "house": "string",
      "apartment": "string|null",
      "zip": "string|null",
      "isDefault": "boolean"
    }
  ]
  ```

**POST /profile/addresses**
- Описание: Добавить новый адрес.
- Тело запроса:
  ```json
  {
    "city": "string",
    "street": "string",
    "house": "string",
    "apartment": "string|null",
    "zip": "string|null",
    "isDefault": "boolean"
  }
  ```
- Ответ:
  ```json
  {
    "id": "string",
    "success": true
  }
  ```

**PUT /profile/addresses/{id}**
- Описание: Изменить адрес.
- Тело запроса: (поля как при добавлении)
- Ответ:
  ```json
  {
    "success": true
  }
  ```

**DELETE /profile/addresses/{id}**
- Описание: Удалить адрес.
- Ответ:
  ```json
  {
    "success": true
  }
  ```

---

## 8. Способы оплаты

**GET /profile/payments**
- Описание: Получить список сохранённых способов оплаты пользователя.
- Ответ:
  ```json
  [
    {
      "id": "string",
      "type": "card|other",
      "maskedNumber": "string",
      "holder": "string",
      "expires": "string"
    }
  ]
  ```

**POST /profile/payments**
- Описание: Добавить/сохранить новую карту или способ оплаты.
- Тело запроса:
  ```json
  {
    "type": "card|other",
    "number": "string",
    "holder": "string",
    "expires": "string",
    "cvc": "string"
  }
  ```
- Ответ:
  ```json
  {
    "id": "string",
    "success": true
  }
  ```

**DELETE /profile/payments/{id}**
- Описание: Удалить способ оплаты.
- Ответ:
  ```json
  {
    "success": true
  }
  ```

---

## 9. Поддержка

**GET /profile/support**
- Описание: Получить историю обращений пользователя.
- Ответ:
  ```json
  [
    {
      "ticketId": "string",
      "subject": "string",
      "status": "open|closed",
      "createdAt": "string"
    }
  ]
  ```

**GET /profile/support/{ticketId}/messages**
- Описание: Получить сообщения в обращении (чат с поддержкой).
- Ответ:
  ```json
  [
    {
      "messageId": "string",
      "author": "user|support",
      "text": "string",
      "createdAt": "string"
    }
  ]
  ```

**POST /profile/support/{ticketId}/messages**
- Описание: Добавить сообщение в тикет поддержки.
- Тело запроса:
  ```json
  {
    "text": "string"
  }
  ```
- Ответ:
  ```json
  {
    "success": true
  }
  ```

**POST /profile/support**
- Описание: Создать новый тикет в поддержку.
- Тело запроса:
  ```json
  {
    "subject": "string",
    "text": "string"
  }
  ```
- Ответ:
  ```json
  {
    "ticketId": "string",
    "success": true
  }
  ```

---

## 10. Настройки уведомлений

**GET /profile/notifications**
- Описание: Получить текущие настройки уведомлений пользователя.
- Ответ:
  ```json
  {
    "email": "boolean",
    "sms": "boolean",
    "push": "boolean",
    "frequency": "immediate|daily|weekly"
  }
  ```

**PUT /profile/notifications**
- Описание: Обновить настройки уведомлений пользователя.
- Тело запроса:
  ```json
  {
    "email": "boolean",
    "sms": "boolean",
    "push": "boolean",
    "frequency": "immediate|daily|weekly"
  }
  ```
- Ответ:
  ```json
  {
    "success": true
  }
  ```

---

## 11. Безопасность

**GET /profile/security/logins**
- Описание: Получить историю входов пользователя (дата, устройство, IP).
- Ответ:
  ```json
  [
    {
      "timestamp": "string",
      "ip": "string",
      "device": "string"
    }
  ]
  ```

**GET /profile/security/devices**
- Описание: Получить список устройств, с которых выполнялся вход.
- Ответ:
  ```json
  [
    {
      "deviceId": "string",
      "name": "string",
      "lastActive": "string"
    }
  ]
  ```

**DELETE /profile/security/devices/{deviceId}**
- Описание: Удалить устройство из доверенных (выход с устройства).
- Ответ:
  ```json
  {
    "success": true
  }
  ```

**GET /profile/security/2fa**
- Описание: Получить статус двухфакторной аутентификации.
- Ответ:
  ```json
  {
    "enabled": "boolean",
    "methods": ["app","sms"]
  }
  ```

**POST /profile/security/2fa/setup**
- Описание: Начать настройку 2FA (вернуть секрет, QR и т.д.).
- Ответ:
  ```json
  {
    "secret": "string",
    "qrCodeUrl": "string"
  }
  ```

**POST /profile/security/2fa/activate**
- Описание: Активировать 2FA после ввода кода.
- Тело запроса:
  ```json
  {
    "code": "string"
  }
  ```
- Ответ:
  ```json
  {
    "success": true
  }
  ```

**POST /profile/security/2fa/disable**
- Описание: Отключить 2FA.
- Ответ:
  ```json
  {
    "success": true
  }
  ```

---