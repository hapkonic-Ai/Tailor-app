# Phase 4 — Core Feature Screens Tasks

**Duration:** Weeks 10–14
**Team:** 2 Developers + QA
**Status:** Complete
**Started:** 2026-03-09
**Completed:** 2026-03-09

---

## 4.1 Customer Management

### Use Cases
- [ ] `GetCustomersUseCase.kt` (paginated, 20/page)
- [ ] `GetCustomerByIdUseCase.kt`
- [ ] `AddCustomerUseCase.kt`
- [ ] `UpdateCustomerUseCase.kt`
- [ ] `SearchCustomersUseCase.kt` (by name and phone)

### ViewModels & States
- [ ] `CustomerListUiState.kt`
- [ ] `CustomerListViewModel.kt` (search, pagination, pull-to-refresh)
- [ ] `AddCustomerViewModel.kt` (form validation: name required, phone format, address required)
- [ ] `CustomerDetailViewModel.kt` (load customer + latest measurement + order history)

### Screens
- [ ] `CustomerListScreen.kt`:
  - [ ] `LazyColumn` with search bar
  - [ ] Paginated loading (20 items/page) + load more
  - [ ] Pull-to-refresh
  - [ ] Each item: name, phone, last order date
  - [ ] Tap → Customer Detail
  - [ ] FAB (Admin only) → Add Customer
- [ ] `AddCustomerScreen.kt`:
  - [ ] Name, phone (format validation), address fields
  - [ ] Real-time validation feedback
  - [ ] Save → SQLDelight write + enqueue sync
- [ ] `CustomerDetailScreen.kt`:
  - [ ] Customer info header
  - [ ] Measurement summary card (latest values)
  - [ ] Order history list (most recent first)
  - [ ] Action buttons: Add Measurement, Create Order, Edit Customer

## 4.2 Order Management

### Use Cases
- [ ] `GetOrdersUseCase.kt`
- [ ] `GetOrdersByStatusUseCase.kt`
- [ ] `GetOrdersByTailorUseCase.kt`
- [ ] `CreateOrderUseCase.kt`
- [ ] `UpdateOrderStatusUseCase.kt`
- [ ] `GetOrdersDueTodayUseCase.kt`
- [ ] `GetOrderByIdUseCase.kt`

### ViewModels & States
- [ ] `OrderListUiState.kt`
- [ ] `OrderListViewModel.kt` (status filter chips, pagination)
- [ ] `CreateOrderViewModel.kt` (customer search, tailor dropdown, date picker, image upload)
- [ ] `OrderDetailViewModel.kt` (status update, role-based action visibility)

### Screens
- [ ] `OrderListScreen.kt`:
  - [ ] Status filter chips: Pending, In Progress, Ready, Delivered
  - [ ] Secondary filters: assigned tailor, date range
  - [ ] Paginated with `limit(20)` + load more
- [ ] `CreateOrderScreen.kt`:
  - [ ] Customer searchable dropdown
  - [ ] Tailor dropdown showing workload count
  - [ ] Delivery date picker
  - [ ] Price input
  - [ ] Cloth image + design image upload (Firebase Storage)
  - [ ] Notes text field
- [ ] `OrderDetailScreen.kt`:
  - [ ] Full order info with status timeline
  - [ ] Customer info section (tap to navigate)
  - [ ] Tailor info section
  - [ ] Images loaded from Firebase Storage URLs (cached via Coil)
  - [ ] Admin actions: Update Status, Reassign Tailor, Edit, Delete
  - [ ] Tailor actions: Advance Status (next step only)

### Order Status Workflow
- [ ] Implement `PENDING → IN_PROGRESS → READY → DELIVERED` flow
- [ ] Admin can set any status
- [ ] Tailor can only advance to next status (not skip, not go back)

## 4.3 Measurement Module

### Use Cases
- [ ] `GetMeasurementsUseCase.kt` (by customer)
- [ ] `AddMeasurementUseCase.kt`
- [ ] `UpdateMeasurementUseCase.kt`

### ViewModels & States
- [ ] `MeasurementFormViewModel.kt` (numeric validation, units toggle)

### Screens
- [ ] `MeasurementFormScreen.kt`:
  - [ ] Numeric fields: Shoulder, Chest, Waist, Hip, Sleeve Length, Shirt Length, Pant Length
  - [ ] cm/inches toggle
  - [ ] Input validation (reasonable ranges, e.g. shoulder 10–60 cm)
  - [ ] Free-text notes field
- [ ] Measurement history list on `CustomerDetailScreen` (timestamps, tap for full detail)

## 4.4 Tailor Management

### Use Cases
- [ ] `GetTailorsUseCase.kt`
- [ ] `AssignTailorUseCase.kt`
- [ ] `GetTailorWorkloadUseCase.kt`

### ViewModels & States
- [ ] `TailorListViewModel.kt`
- [ ] `TailorOrdersViewModel.kt`

### Screens
- [ ] `TailorListScreen.kt` (Admin only):
  - [ ] All tailors: name, specialization, active order count
  - [ ] Tap → Tailor Orders
  - [ ] Add/edit tailor profiles
- [ ] `TailorOrdersScreen.kt` (primary Tailor-role screen):
  - [ ] Only orders assigned to logged-in tailor
  - [ ] Grouped by status with counts
  - [ ] Quick action: tap to advance order status

## 4.5 Navigation Architecture

- [ ] Add navigation library (Voyager or Decompose)
- [ ] Create `Screen.kt` sealed class with all routes
- [ ] Create `AppNavGraph.kt` with role-based routing:
  - [ ] Admin: Dashboard, Customers, Orders, Tailors, all detail screens
  - [ ] Tailor: Dashboard (limited), Tailor Orders (own), Order Detail (own)
  - [ ] Unauthenticated: Login only
- [ ] Create `RoleGuard.kt` navigation wrapper (block unauthorized access)
- [ ] Implement deep links: `app://orders/{orderId}`, `app://customers/{customerId}`

## 4.6 Shared UI Components

- [ ] `KpiCard.kt`
- [ ] `OrderStatusChip.kt`
- [ ] `SearchBar.kt`
- [ ] `PaginatedList.kt`
- [ ] `ImagePicker.kt` (expect/actual for Android + iOS)
- [ ] `DatePickerField.kt`
- [ ] `EmptyState.kt`
- [ ] `LoadingSkeleton.kt`
- [ ] `ErrorState.kt` with retry button

## 4.7 Theme Setup

- [ ] `AppTheme.kt` (Material 3 theming)
- [ ] `Color.kt`
- [ ] `Typography.kt`
- [ ] `Shape.kt`

---

## Deliverables Checklist

- [ ] Customer list, add/edit, and detail screens fully functional
- [ ] Order list, create, and detail screens with status workflow
- [ ] Measurement form linked to customers with history view
- [ ] Tailor list and tailor-specific order views
- [ ] Shared navigation graph with role-based routing
- [ ] All screens connected to ViewModels with StateFlow-driven UI
