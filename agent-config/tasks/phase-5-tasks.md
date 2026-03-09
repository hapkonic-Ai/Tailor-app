# Phase 5 — Dashboard, Analytics & Search Tasks

**Duration:** Weeks 15–17
**Team:** Frontend Developer
**Status:** Not Started
**Started:** —
**Completed:** —

---

## 5.1 Dashboard KPI Cards

- [ ] Create `DashboardUiState.kt` (`ordersToday`, `pendingOrders`, `readyOrders`, `tailorWorkload`, `isLoading`, `lastSyncTime`)
- [ ] Create `TailorWorkload` data class (`tailorName`, `activeOrderCount`)
- [ ] Create `GetDashboardMetricsUseCase.kt`:
  - [ ] Orders today → `orderQueries.getDueToday()`
  - [ ] Pending count → `orderQueries.countByStatus(PENDING)`
  - [ ] Ready count → `orderQueries.countByStatus(READY)`
  - [ ] Tailor workload → `orderQueries.countByTailor()`
- [ ] Create `DashboardViewModel.kt` (all metrics from local SQLDelight, instant render)
- [ ] Create `DashboardScreen.kt`:
  - [ ] Four KPI `KpiCard` components in a grid
  - [ ] Tailor workload section (list of tailors with active order counts)
  - [ ] Last sync timestamp display
  - [ ] Pull-to-refresh to trigger manual sync

## 5.2 Advanced Search & Filters

- [ ] Global search across customers (name + phone) using `LIKE` queries
- [ ] Global search across orders (order ID + customer name)
- [ ] Date range picker filter (e.g., orders due this week)
- [ ] Multi-status filter chips (toggle Pending, In Progress, Ready, Delivered independently)
- [ ] All search runs locally on SQLDelight for instant results (no Firestore round-trip)
- [ ] Debounce search input (300ms) to avoid excessive queries

## 5.3 Revenue Reports

- [ ] Add `getOrdersInDateRange(startDate, endDate)` to `OrderRepository`
- [ ] Add corresponding SQLDelight query to `orders.sq`
- [ ] Create `RevenueReport` data class (`totalRevenue`, `orderCount`, `byStatus`, `byDay`)
- [ ] Create `GetRevenueReportUseCase.kt`:
  - [ ] Total revenue in date range
  - [ ] Order count in date range
  - [ ] Revenue grouped by status
  - [ ] Revenue grouped by day
- [ ] Create report UI:
  - [ ] Date range selector
  - [ ] Summary totals card
  - [ ] Revenue breakdown by status
  - [ ] Daily revenue list

## 5.4 CSV Export

- [ ] Create `CsvExporter.kt` (expect/actual for file system access per platform)
- [ ] Export columns: Order ID, Customer Name, Tailor Name, Status, Price, Order Date, Delivery Date
- [ ] Generate CSV from filtered order list
- [ ] Platform share sheet integration (share file to email/files/etc.)

---

## Deliverables Checklist

- [ ] Dashboard screen with four KPI cards rendering from local data
- [ ] Global search across customers and orders
- [ ] Advanced filter UI with date range and multi-status selection
- [ ] Revenue and order summary reports
- [ ] Optional CSV export functionality
