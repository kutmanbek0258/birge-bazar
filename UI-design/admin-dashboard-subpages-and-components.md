# Admin Dashboard — Подстраницы и компоненты

---

## Основная страница Admin Dashboard

**URL:** `/admin` или `/admin/dashboard`

---

## Возможные подстраницы (разделы/вкладки):

1. **Главная / Обзор (Overview)**
   - Сводная статистика по пользователям, продавцам, заказам, товарам, финансам.
   - Графики и ключевые показатели.

2. **Пользователи (Users)**
   - Список всех пользователей, поиск, фильтры, блокировка/разблокировка, просмотр профиля.

3. **Продавцы (Sellers)**
   - Список продавцов, статус, модерация, заявки на регистрацию, действия с продавцами.

4. **Товары (Products)**
   - Все товары на платформе, модерация, скрытие/удаление, фильтры.

5. **Заказы (Orders)**
   - Список всех заказов, фильтрация по статусу, детализация, отмена/возврат.

6. **Отзывы (Reviews)**
   - Модерация отзывов, жалобы, удаление/скрытие.

7. **Финансы (Finance)**
   - Обороты, выплаты продавцам, комиссии, отчёты, ручные операции.

8. **Модерация (Moderation)**
   - Жалобы, флаги, проблемные заказы/товары/пользователи.

9. **Поддержка (Support/Tickets)**
   - Просмотр и обработка обращений пользователей, чат, статус тикетов.

10. **Контент (Content/Pages)**
    - Управление статичными страницами, баннерами, новостями, FAQ.

11. **Логи/Активность (Logs/Activity)**
    - История действий админов и системы, аудит.

12. **Настройки платформы (Settings)**
    - Основные параметры, email/SMS, роли, права, интеграции.

---

## Примеры URL подстраниц:

- `/admin/overview`
- `/admin/users`
- `/admin/sellers`
- `/admin/products`
- `/admin/orders`
- `/admin/reviews`
- `/admin/finance`
- `/admin/moderation`
- `/admin/support`
- `/admin/content`
- `/admin/logs`
- `/admin/settings`

---

## Основные компоненты (модули):

- **AdminStatsCards** — Карточки KPI (пользователи, продажи, новые заказы и т.д.)
- **ChartsBlock** — Диаграммы и графики
- **UserList** — Список пользователей
- **UserCard/AdminUserDetail** — Детали пользователя, действия
- **SellerList** — Список продавцов
- **SellerCard/SellerDetail** — Детали продавца, модерация
- **ProductList** — Все товары
- **ProductModerationPanel** — Модерация товаров
- **OrderList** — Все заказы
- **OrderDetail** — Детали заказа
- **ReviewList** — Модерация отзывов
- **FinanceDashboard** — Обороты, выплаты, комиссии
- **PayoutsList** — Выплаты продавцам
- **ModerationQueue** — Жалобы, проблемные объекты
- **SupportTicketsList** — Обращения пользователей, чат
- **ContentPageList** — Контент, новости, баннеры
- **LogList** — Логи и аудит
- **SettingsForm** — Настройки платформы, интеграции

---

## Пример структуры (JSX/Pug-like):

```
<AdminDashboard>
  <SidebarNavigation />
  <Header />
  <AdminStatsCards />
  <ChartsBlock />
  <Tabs>
    <Tab title="Пользователи"><UserList /></Tab>
    <Tab title="Продавцы"><SellerList /></Tab>
    <Tab title="Товары"><ProductList /><ProductModerationPanel /></Tab>
    <Tab title="Заказы"><OrderList /></Tab>
    <Tab title="Отзывы"><ReviewList /></Tab>
    <Tab title="Финансы"><FinanceDashboard /><PayoutsList /></Tab>
    <Tab title="Модерация"><ModerationQueue /></Tab>
    <Tab title="Поддержка"><SupportTicketsList /></Tab>
    <Tab title="Контент"><ContentPageList /></Tab>
    <Tab title="Логи"><LogList /></Tab>
    <Tab title="Настройки"><SettingsForm /></Tab>
  </Tabs>
</AdminDashboard>
```

---

## Краткая карта подстраниц и компонентов:

| Раздел           | Ключевые компоненты                                      |
|------------------|----------------------------------------------------------|
| Главная/Обзор    | AdminStatsCards, ChartsBlock                             |
| Пользователи     | UserList, UserCard/AdminUserDetail                       |
| Продавцы         | SellerList, SellerCard/SellerDetail                      |
| Товары           | ProductList, ProductModerationPanel                      |
| Заказы           | OrderList, OrderDetail                                   |
| Отзывы           | ReviewList                                               |
| Финансы          | FinanceDashboard, PayoutsList                            |
| Модерация        | ModerationQueue                                          |
| Поддержка        | SupportTicketsList                                       |
| Контент          | ContentPageList                                          |
| Логи             | LogList                                                  |
| Настройки        | SettingsForm                                             |

---

Если нужен SVG-макет для одной из подстраниц — уточните!