# Seller Dashboard — Подстраницы и компоненты

---

## Основная страница Seller Dashboard

**URL:** `/seller` или `/seller/dashboard`

---

## Возможные подстраницы (разделы/вкладки):

1. **Главная / Обзор (Overview)**
   - Краткая статистика: продажи за день/месяц, количество заказов, просмотров, новые сообщения.
   - Графики/диаграммы.

2. **Товары (Products)**
   - Список товаров, статус (в наличии, скрыт), редактирование/удаление.
   - Кнопка “Добавить товар”.

3. **Заказы (Orders)**
   - Список заказов, фильтр по статусу (новый, в обработке, отправлен, завершён, отменён).
   - Просмотр деталей заказа, смена статуса, печать накладной.

4. **Отзывы (Reviews)**
   - Список отзывов на товары, рейтинг, возможность ответить на отзыв.

5. **Сообщения (Messages/Чат)**
   - Переписка с покупателями, уведомления о новых сообщениях.

6. **Аналитика (Analytics)**
   - Продажи, конверсия, просмотры, возвраты, отчёты.

7. **Финансы (Finance/Payouts)**
   - Баланс, история выплат, реквизиты, запросить выплату.

8. **Реклама и продвижение (Promotions/Ads)**
   - Запуск/мониторинг акций, купоны, реклама.

9. **Настройки магазина (Shop Settings)**
   - Основная информация, логотип, расписание, политики, адреса.

10. **Сотрудники (Team/Staff)**
    - Управление сотрудниками, роли, доступы.

---

## Примеры URL подстраниц:

- `/seller/overview`
- `/seller/products`
- `/seller/orders`
- `/seller/reviews`
- `/seller/messages`
- `/seller/analytics`
- `/seller/finance`
- `/seller/promotions`
- `/seller/settings`
- `/seller/team`

---

## Основные компоненты (модули):

- **StatsCards** — Маленькие карточки с ключевыми показателями
- **ChartsBlock** — Графики и диаграммы
- **ProductList** — Список товаров
- **ProductCard** — Карточка товара, статус, быстрые действия
- **OrderList** — Список заказов с фильтрами
- **OrderCard/OrderDetail** — Детали заказа, смена статуса
- **ReviewList** — Список отзывов, форма ответа
- **MessagesList** — Список чатов/диалогов
- **ChatWindow** — Окно переписки
- **AnalyticsCharts** — Расширенная аналитика
- **PayoutsList** — История выплат
- **PayoutRequestForm** — Форма запроса выплаты
- **PromotionList** — Список акций/рекламы
- **PromotionForm** — Создание/редактирование акции
- **ShopSettingsForm** — Настройки магазина
- **TeamList** — Сотрудники
- **StaffInviteForm** — Пригласить сотрудника

---

## Пример структуры (JSX/Pug-like):

```
<SellerDashboard>
  <SidebarNavigation />
  <Header />
  <StatsCards />
  <ChartsBlock />
  <Tabs>
    <Tab title="Товары"><ProductList /></Tab>
    <Tab title="Заказы"><OrderList /></Tab>
    <Tab title="Отзывы"><ReviewList /></Tab>
    <Tab title="Сообщения"><MessagesList /><ChatWindow /></Tab>
    <Tab title="Аналитика"><AnalyticsCharts /></Tab>
    <Tab title="Финансы"><PayoutsList /><PayoutRequestForm /></Tab>
    <Tab title="Реклама"><PromotionList /><PromotionForm /></Tab>
    <Tab title="Настройки"><ShopSettingsForm /></Tab>
    <Tab title="Сотрудники"><TeamList /><StaffInviteForm /></Tab>
  </Tabs>
</SellerDashboard>
```

---

## Краткая карта подстраниц и компонентов:

| Раздел             | Ключевые компоненты                        |
|--------------------|--------------------------------------------|
| Главная/Обзор      | StatsCards, ChartsBlock                    |
| Товары             | ProductList, ProductCard                   |
| Заказы             | OrderList, OrderCard/OrderDetail           |
| Отзывы             | ReviewList                                 |
| Сообщения          | MessagesList, ChatWindow                   |
| Аналитика          | AnalyticsCharts                            |
| Финансы            | PayoutsList, PayoutRequestForm             |
| Реклама            | PromotionList, PromotionForm               |
| Настройки          | ShopSettingsForm                           |
| Сотрудники         | TeamList, StaffInviteForm                  |

---

Если нужен SVG-макет для одной из вкладок или главной — уточните!