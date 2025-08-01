# WMS (Warehouse Management System) как отдельный поддомен

---

## Пример поддомена

- **URL:** `https://wms.yourshop.kz`  
- **Страницы WMS доступны только при авторизации, интерфейс отличается от seller/admin/personal.**

---

## Основные страницы (разделы WMS):

1. **Главная / Дашборд (Dashboard)**
   - Краткая статистика: остатки, поступления, отгрузки, задания.
   - Диаграммы, уведомления.

2. **Приёмка товара (Inbound/Receiving)**
   - Список поступлений, приём новых партий, сканирование, подтверждение.
   - Статус: ожидает, в процессе, завершено.

3. **Хранение (Storage/Inventory)**
   - Складские остатки, поиск по артикулу, ячейки, движение товара, инвентаризации.
   - Перемещения между ячейками.

4. **Сборка/Комплектация (Picking/Packing)**
   - Задания на сборку, статусы (новое, в работе, собрано), маршрут сборки.
   - Упаковка, печать этикеток.

5. **Отгрузка (Outbound/Shipping)**
   - Список отгрузок, статусы (готово к отгрузке, отправлено), формирование ТТН.
   - Контроль отправок.

6. **Инвентаризация (Stocktaking/Audit)**
   - Создание и выполнение инвентаризаций, история, результаты.

7. **Перемещения (Transfers)**
   - Внутрискладские и межскладские перемещения, история.

8. **Документы (Documents)**
   - Накладные, акты, печать и просмотр.

9. **Логи действий (Logs)**
   - История операций: кто и что сделал по партиям, заданиям, остаткам.

10. **Настройки (Settings)**
    - Пользователи WMS, права, параметры склада.

---

## Примерные URL поддоменных страниц:

- `https://wms.yourshop.kz/dashboard`
- `https://wms.yourshop.kz/receiving`
- `https://wms.yourshop.kz/inventory`
- `https://wms.yourshop.kz/picking`
- `https://wms.yourshop.kz/shipping`
- `https://wms.yourshop.kz/stocktaking`
- `https://wms.yourshop.kz/transfers`
- `https://wms.yourshop.kz/documents`
- `https://wms.yourshop.kz/logs`
- `https://wms.yourshop.kz/settings`

---

## Основные компоненты (модули):

- **WmsStatsCards** — Карточки с показателями склада
- **InboundList / InboundDetail** — Приёмки и детали
- **InventoryTable** — Остатки склада
- **BinMap / BinCell** — Карта ячеек, детализация по ячейке
- **PickingTasksList / PickingTaskDetail** — Задания на сборку/упаковку
- **OutboundList / OutboundDetail** — Отгрузки, статусы, ТТН
- **StocktakingList / StocktakingForm** — Инвентаризации и формы проведения
- **TransferList / TransferForm** — Перемещения
- **DocumentList / DocumentViewer** — Работа с документами
- **WmsLogList** — Логи операций
- **WmsSettingsForm** — Пользователи, права, параметры склада
- **BarcodeScanner** — Сканирование товаров/этикеток (модальный компонент)

---

## Пример структуры (JSX/Pug-like):

```
<WmsLayout>
  <SidebarNavigation />
  <Header />
  <WmsStatsCards />
  <Tabs>
    <Tab title="Приёмка"><InboundList /></Tab>
    <Tab title="Хранение"><InventoryTable /><BinMap /></Tab>
    <Tab title="Сборка"><PickingTasksList /></Tab>
    <Tab title="Отгрузка"><OutboundList /></Tab>
    <Tab title="Инвентаризация"><StocktakingList /></Tab>
    <Tab title="Перемещения"><TransferList /></Tab>
    <Tab title="Документы"><DocumentList /></Tab>
    <Tab title="Логи"><WmsLogList /></Tab>
    <Tab title="Настройки"><WmsSettingsForm /></Tab>
  </Tabs>
  <BarcodeScanner />
</WmsLayout>
```

---

## Краткая карта страниц и компонентов:

| Страница           | Ключевые компоненты                        |
|--------------------|--------------------------------------------|
| Дашборд            | WmsStatsCards                              |
| Приёмка            | InboundList, InboundDetail, BarcodeScanner |
| Хранение           | InventoryTable, BinMap, BinCell            |
| Сборка/Комплектация| PickingTasksList, PickingTaskDetail        |
| Отгрузка           | OutboundList, OutboundDetail               |
| Инвентаризация     | StocktakingList, StocktakingForm           |
| Перемещения        | TransferList, TransferForm                 |
| Документы          | DocumentList, DocumentViewer               |
| Логи               | WmsLogList                                 |
| Настройки          | WmsSettingsForm                            |

---

**Если требуется SVG-макет любой страницы WMS — уточните, какую страницу нарисовать!**