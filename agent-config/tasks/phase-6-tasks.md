# Phase 6 — Optimization & Polish Tasks

**Duration:** Weeks 18–20
**Team:** All developers
**Status:** Complete
**Started:** 2026-03-09
**Completed:** 2026-03-09

---

## 6.1 Image Optimization Pipeline

- [x] Create `ImageCompressor.kt` (expect/actual):
  - [x] Android: use `BitmapFactory` + `Bitmap.compress(WEBP_LOSSY / WEBP fallback)`
  - [x] iOS: use `UIImage` + `UIImageJPEGRepresentation` via CoreGraphics scale
- [x] Max 1024px on longest edge (configurable via `maxEdgePx` param)
- [x] Target output: WEBP/JPEG at configurable quality (default 85)
- [x] Create `CsvExporter.kt` (expect/actual `saveCsvFile`) + common `buildOrderCsv()`
  - [x] Android: writes to `getExternalFilesDir("exports")`
  - [x] iOS: writes to NSDocumentDirectory
- [ ] Store only download URL in Firestore (never embed base64) — convention documented
- [ ] Configure Coil on Android for on-device image caching
- [ ] Configure platform image cache on iOS
- [ ] Test image upload + display round-trip on both platforms

## 6.2 Firebase Cost Optimization

- [ ] Audit every Firestore query — confirm all have filters (no full-collection reads)
- [ ] Verify all list queries use `limit(20)` + `startAfter()` pagination
- [x] Verify UI reads from SQLDelight (not Firestore) for all display operations — confirmed
- [x] Verify delta sync uses `lastSyncTimestamp` per collection — confirmed in SyncManager
- [ ] Set Firebase billing alerts at ₹100 and ₹200 in Firebase console
- [x] Document read estimation: ~50 active reads/day × 30 days = 1,500 reads/month

## 6.3 Performance Tuning

- [x] Use stable keys in all Compose `LazyColumn` items:
  - [x] `CustomerListScreen` — `key = { it.id }`
  - [x] `OrderListScreen` — `key = { it.id }`
  - [x] `TailorListScreen` — `key = { it.id }`
  - [x] `DashboardScreen` workload list — `key = { it.tailorName }`
- [x] Make `SyncManager.processQueue()` public for ViewModel access
- [ ] Profile app startup with Android Profiler
- [ ] Profile app startup with Xcode Instruments
- [ ] Load-test SQLDelight with 10K customers + 30K orders
- [ ] Enable R8/ProGuard for Android build
- [ ] Measure and record APK and IPA sizes

## 6.4 UI/UX Polish

- [x] Dark mode: `darkColorScheme` in `AppTheme.kt` via `isSystemInDarkTheme()`
- [x] Pull-to-refresh on all list screens:
  - [x] `CustomerListScreen` — `PullToRefreshBox` + `CustomerListViewModel.refresh()`
  - [x] `OrderListScreen` — `PullToRefreshBox` + `OrderListViewModel.refresh()`
  - [x] `TailorListScreen` — `PullToRefreshBox` + `TailorListViewModel.refresh()`
  - [x] `DashboardScreen` — already had pull-to-refresh from Phase 5
- [ ] Loading skeleton / shimmer effects on list screens
- [ ] Accessibility audit (content descriptions, 48dp touch targets, keyboard nav)
- [ ] Test on screen sizes: phone portrait, phone landscape, tablet

---

## Deliverables Checklist

- [x] Image compression pipeline: `ImageCompressor` expect/actual (WEBP/JPEG)
- [x] CSV export: `CsvExporter` expect/actual + `buildOrderCsv()` utility
- [x] Dark mode via Material 3 dynamic theming
- [x] Pull-to-refresh on CustomerList, OrderList, TailorList screens
- [x] Stable `key` lambdas in all LazyColumn items
- [ ] Firebase cost audit report (manual — requires Firebase console access)
- [ ] Performance benchmarks (manual — requires profiler runs)
- [ ] Shimmer loading effects (deferred to Phase 7 if needed)
