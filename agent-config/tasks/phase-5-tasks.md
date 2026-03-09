# Phase 5 — Dashboard, Analytics & Search Tasks

**Duration:** Weeks 15–17
**Team:** Frontend Developer
**Status:** Complete
**Started:** 2026-03-09
**Completed:** 2026-03-09

---

## 5.1 Dashboard KPI Cards

- [x] Create `DashboardUiState.kt` (`pendingCount`, `inProgressCount`, `readyCount`, `totalActiveOrders`, `tailorWorkload`, `isLoading`)
- [x] Create `TailorWorkload` data class (`tailorName`, `activeOrderCount`)
- [x] Create `GetDashboardMetricsUseCase.kt`:
  - [x] Pending count → `orderRepo.getCountByStatus(PENDING)`
  - [x] In-Progress count → `orderRepo.getCountByStatus(IN_PROGRESS)`
  - [x] Ready count → `orderRepo.getCountByStatus(READY)`
  - [x] Tailor workload → combine tailor list + active order counts per tailor
- [x] Create `DashboardViewModel.kt` (all metrics from local SQLDelight, instant render, `isSyncing` StateFlow)
- [x] Create `DashboardScreen.kt`:
  - [x] Four KPI `KpiCard` components in 2×2 grid layout
  - [x] Tailor workload section (list of tailors with active order counts)
  - [x] Pull-to-refresh (`PullToRefreshBox`) to trigger manual sync via `syncManager.processQueue()`

## 5.2 Advanced Search & Filters

- [x] Global search across customers (name + phone) using debounced query
- [x] Global search across orders (order ID prefix match)
- [x] Multi-status filter chips on `OrderListScreen` (toggle Pending, In Progress, Ready, Delivered)
- [x] All search runs locally on SQLDelight for instant results
- [x] Debounce search input (300ms) to avoid excessive queries
- [x] `SearchViewModel.kt` with `flatMapLatest` + `debounce(300ms)`
- [x] `SearchScreen.kt` split by Customers / Orders sections

## 5.3 Revenue Reports

- [x] Add `getOrdersInDateRange(startDate, endDate)` to `OrderRepository`
- [x] Create `RevenueReport` data class (`totalRevenue`, `orderCount`, `byStatus`, `byDay`)
- [x] Create `DayRevenue` data class
- [x] Create `GetRevenueReportUseCase.kt`:
  - [x] Total revenue in date range
  - [x] Order count in date range
  - [x] Revenue grouped by status
  - [x] Revenue grouped by day (86_400_000 ms bucket)
- [x] Create `RevenueViewModel.kt` (date range StateFlow + flatMapLatest, `setThisWeek`, `setThisMonth`)
- [x] Create `RevenueScreen.kt`:
  - [x] Date range selector (This Week / This Month filter chips)
  - [x] Summary totals card
  - [x] Revenue breakdown by status
  - [x] Daily revenue list
- [x] Wire `Screen.Revenue` in `MainScaffold.kt`

## 5.4 CSV Export

- [ ] Create `CsvExporter.kt` (expect/actual for file system access per platform)
- [ ] Export columns: Order ID, Customer Name, Tailor Name, Status, Price, Order Date, Delivery Date
- [ ] Generate CSV from filtered order list
- [ ] Platform share sheet integration (share file to email/files/etc.)

---

## Deliverables Checklist

- [x] Dashboard screen with KPI cards rendering from local data
- [x] Global search across customers and orders
- [x] Advanced filter UI with multi-status selection on OrderListScreen
- [x] Revenue and order summary reports
- [ ] CSV export functionality (deferred to Phase 6)
