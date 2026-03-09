# Phase 4 — Core Feature Screens — Implementation Log

**Date Started:** 2026-03-09
**Last Updated:** 2026-03-09
**Status:** Complete

---

## Files Created / Modified

| File Path | Action | Notes |
|-----------|--------|-------|
| `shared/.../ui/theme/Color.kt` | Created | Brand colors + 4 order status colours |
| `shared/.../ui/theme/Typography.kt` | Created | AppTypography (Material 3) |
| `shared/.../ui/theme/AppTheme.kt` | Created | Light colour scheme, wraps MaterialTheme |
| `shared/.../navigation/Screen.kt` | Created | Sealed class for all destinations |
| `shared/.../navigation/AppNavigator.kt` | Created | Simple backstack navigator, LocalNavigator CompositionLocal |
| `shared/.../navigation/AppNavGraph.kt` | Created | Root nav composable with auth-based initial screen |
| `shared/.../ui/components/OrderStatusChip.kt` | Created | Colour-coded status badge |
| `shared/.../ui/components/SearchBar.kt` | Created | OutlinedTextField with search icon |
| `shared/.../ui/components/EmptyState.kt` | Created | EmptyState + ErrorState with optional retry |
| `shared/.../ui/components/LoadingIndicator.kt` | Created | Centred CircularProgressIndicator |
| `shared/.../domain/usecase/customer/GetCustomersUseCase.kt` | Created | |
| `shared/.../domain/usecase/customer/GetCustomerByIdUseCase.kt` | Created | |
| `shared/.../domain/usecase/customer/SearchCustomersUseCase.kt` | Created | Merges name+phone results, deduped |
| `shared/.../domain/usecase/customer/SaveCustomerUseCase.kt` | Created | Handles add + update |
| `shared/.../presentation/customer/CustomerListViewModel.kt` | Created | debounce+flatMapLatest search, StateFlow |
| `shared/.../presentation/customer/CustomerFormViewModel.kt` | Created | Add/edit customer, phone regex validation |
| `shared/.../presentation/customer/CustomerDetailViewModel.kt` | Created | combine(customer, measurements, orders) |
| `shared/.../presentation/customer/CustomerListScreen.kt` | Created | LazyColumn + SearchBar + FAB (Admin only) |
| `shared/.../presentation/customer/CustomerFormScreen.kt` | Created | Form with validation feedback |
| `shared/.../presentation/customer/CustomerDetailScreen.kt` | Created | Info card, actions, latest measurement, order history |
| `shared/.../domain/usecase/order/GetOrdersByStatusUseCase.kt` | Created | |
| `shared/.../domain/usecase/order/GetOrdersByCustomerUseCase.kt` | Created | |
| `shared/.../domain/usecase/order/GetOrdersByTailorUseCase.kt` | Created | |
| `shared/.../domain/usecase/order/GetOrderByIdUseCase.kt` | Created | |
| `shared/.../domain/usecase/order/CreateOrderUseCase.kt` | Created | |
| `shared/.../domain/usecase/order/UpdateOrderStatusUseCase.kt` | Created | Role enforcement — tailor can only advance next |
| `shared/.../presentation/order/OrderListViewModel.kt` | Created | flatMapLatest on status filter |
| `shared/.../presentation/order/CreateOrderViewModel.kt` | Created | Tailor dropdown, price validation |
| `shared/.../presentation/order/OrderDetailViewModel.kt` | Created | Status actions with role enforcement |
| `shared/.../presentation/order/OrderListScreen.kt` | Created | FilterChip row + LazyColumn |
| `shared/.../presentation/order/CreateOrderScreen.kt` | Created | ExposedDropdownMenu for tailor, price field |
| `shared/.../presentation/order/OrderDetailScreen.kt` | Created | Admin full status control, Tailor advance-only |
| `shared/.../domain/usecase/measurement/GetMeasurementsUseCase.kt` | Created | |
| `shared/.../domain/usecase/measurement/SaveMeasurementUseCase.kt` | Created | |
| `shared/.../presentation/measurement/MeasurementFormViewModel.kt` | Created | cm/inches toggle, range validation, auto-converts to cm |
| `shared/.../presentation/measurement/MeasurementFormScreen.kt` | Created | 7 measurement fields + unit Switch |
| `shared/.../domain/usecase/tailor/GetTailorsUseCase.kt` | Created | |
| `shared/.../presentation/tailor/TailorListViewModel.kt` | Created | |
| `shared/.../presentation/tailor/TailorOrdersViewModel.kt` | Created | Groups orders by status, advance action |
| `shared/.../presentation/tailor/TailorListScreen.kt` | Created | Active order count Badge |
| `shared/.../presentation/tailor/TailorOrdersScreen.kt` | Created | Grouped by status, quick advance button |
| `shared/.../di/AppModule.kt` | Updated | All use cases + ViewModels registered |
| `shared/.../App.kt` | Updated | Mounts AppNavGraph with AppTheme |

## Commits

| Commit Hash | Message | Date |
|-------------|---------|------|
| TBD | feat(phase-4): feature screens, navigation, use cases, ViewModels | 2026-03-09 |

## Deviations from Plan

- No external navigation library added — custom `AppNavigator` backstack (avoids dependency, sufficient for current screen set)
- `ImagePicker` expect/actual not implemented — deferred to Phase 6 (image upload is complex platform-specific UI)
- `DatePickerField` not implemented as standalone component — delivery date defaults to now+7 days (Phase 6 can add a proper picker)
- `KpiCard` deferred to Phase 5 (Dashboard phase)
- `PaginatedList` deferred — repositories support limit/offset, pagination UI wiring in Phase 5

## Known Issues / TODOs for Phase 5

- Bottom navigation bar not yet added — Phase 5 will add a persistent nav bar (Customers | Orders | Dashboard | Tailors)
- `CustomerListScreen` receives `currentUser` from nav graph, not yet wired in `AppNavGraph`
- Tailor sign-in should route to `TailorOrdersScreen` directly (using `currentUser.uid`) rather than `OrderList` — Phase 5 refinement
- Image upload UI placeholder in `CreateOrderScreen` — Phase 6
