# User Profile — Подстраницы и компоненты

---

## Основная страница профиля пользователя

**URL:** `/profile` или `/user/:id`

---

## Возможные подстраницы (вкладки/шаги):

1. **Мои данные / Личная информация**
   - Просмотр и редактирование имени, e-mail, телефона, даты рождения, пола, аватара.
   - Изменение пароля.

2. **Мои заказы (Order History)**
   - Список всех заказов, статусы, детали заказа, повторить заказ.

3. **Избранное (Favorites/Wishlist)**
   - Сохранённые товары, быстрый переход к карточке товара.

4. **Отзывы (My Reviews)**
   - Список оставленных отзывов, возможность редактировать/удалять.

5. **Адреса доставки (Shipping Addresses)**
   - Список адресов, добавление/редактирование/удаление.

6. **Способы оплаты (Payment Methods)**
   - Сохранённые карты, добавление/удаление способа оплаты.

7. **Поддержка / Мои обращения**
   - История обращений, чат с поддержкой, открыть новый тикет.

8. **Настройки уведомлений**
   - Подписки на email/SMS/push, частота уведомлений.

9. **Безопасность**
   - История входов, 2FA, управление устройствами.

---

## Примеры URL подстраниц:

- `/profile/info`
- `/profile/orders`
- `/profile/favorites`
- `/profile/reviews`
- `/profile/addresses`
- `/profile/payments`
- `/profile/support`
- `/profile/notifications`
- `/profile/security`

---

## Основные компоненты (модули):

- **UserAvatar** — Аватар и кнопка смены
- **UserInfoForm** — Форма личных данных
- **ChangePasswordForm** — Смена пароля
- **OrderList** — Список заказов
- **OrderCard/OrderDetail** — Карточка заказа, детали
- **FavoritesList** — Список избранных товаров
- **ReviewList** — Мои отзывы
- **AddressList** — Список адресов доставки
- **AddressForm** — Форма добавления/редактирования адреса
- **PaymentMethodList** — Список способов оплаты
- **PaymentMethodForm** — Добавить/удалить карту
- **SupportTicketsList** — История обращений
- **SupportChat** — Чат с поддержкой
- **NotificationSettingsForm** — Настройка уведомлений
- **SecuritySettings** — История входов, 2FA, устройства

---

## Пример структуры (JSX/Pug-like):

```
<UserProfile>
  <UserAvatar />
  <Tabs>
    <Tab title="Мои данные"><UserInfoForm /><ChangePasswordForm /></Tab>
    <Tab title="Мои заказы"><OrderList /></Tab>
    <Tab title="Избранное"><FavoritesList /></Tab>
    <Tab title="Отзывы"><ReviewList /></Tab>
    <Tab title="Адреса"><AddressList /><AddressForm /></Tab>
    <Tab title="Оплата"><PaymentMethodList /><PaymentMethodForm /></Tab>
    <Tab title="Поддержка"><SupportTicketsList /><SupportChat /></Tab>
    <Tab title="Уведомления"><NotificationSettingsForm /></Tab>
    <Tab title="Безопасность"><SecuritySettings /></Tab>
  </Tabs>
</UserProfile>
```

---

## Краткая карта подстраниц и компонентов:

| Подстраница           | Ключевые компоненты                  |
|---------------------- |--------------------------------------|
| Мои данные           | UserAvatar, UserInfoForm, ChangePasswordForm |
| Мои заказы           | OrderList, OrderCard                 |
| Избранное            | FavoritesList                        |
| Отзывы               | ReviewList                           |
| Адреса               | AddressList, AddressForm             |
| Оплата               | PaymentMethodList, PaymentMethodForm |
| Поддержка            | SupportTicketsList, SupportChat      |
| Уведомления          | NotificationSettingsForm             |
| Безопасность         | SecuritySettings                     |

---

Если нужен SVG-макет для одной из вкладок — уточните!