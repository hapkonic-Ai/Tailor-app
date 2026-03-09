# Phase 6 — Optimization & Polish Tasks

**Duration:** Weeks 18–20
**Team:** All developers
**Status:** Not Started
**Started:** —
**Completed:** —

---

## 6.1 Image Optimization Pipeline

- [ ] Create `ImageCompressor.kt` (expect/actual):
  - [ ] Android: use `BitmapFactory` + `Bitmap.compress(WEBP)`
  - [ ] iOS: use `UIImage` + `jpegData` or WEBP via `ImageIO`
- [ ] Max 1024px on longest edge
- [ ] Target output: WEBP format, 120–150 KB
- [ ] Storage paths: `orders/{orderId}/cloth.webp`, `orders/{orderId}/design.webp`
- [ ] Store only download URL in Firestore (never embed base64)
- [ ] Configure Coil on Android for on-device image caching
- [ ] Configure platform image cache on iOS
- [ ] Test image upload + display round-trip on both platforms

## 6.2 Firebase Cost Optimization

- [ ] Audit every Firestore query — confirm all have filters (no full-collection reads)
- [ ] Verify all list queries use `limit(20)` + `startAfter()` pagination
- [ ] Verify UI reads from SQLDelight (not Firestore) for all display operations
- [ ] Verify delta sync uses `lastSyncTimestamp` per collection
- [ ] Set Firebase billing alerts at ₹100 and ₹200 in Firebase console
- [ ] Document read estimation: ~50 active reads/day × 30 days = 1,500 reads/month
- [ ] Run cost audit report and log findings in [phase-6-impl.md](../implementations/phase-6-impl.md)

## 6.3 Performance Tuning

### Targets
- App startup to interactive: < 2 seconds
- SQLDelight query response (10K records): < 100ms
- List scroll (30K orders): 60fps with LazyColumn
- Sync queue processing: < 5 seconds for 50 pending items
- APK size: < 15 MB
- IPA size: < 25 MB

### Tasks
- [ ] Profile app startup with Android Profiler
- [ ] Profile app startup with Xcode Instruments
- [ ] Use stable keys in all Compose `LazyColumn` items for efficient recomposition
- [ ] Load-test SQLDelight with 10K customers + 30K orders (write test data script)
- [ ] Benchmark all critical queries and document results
- [ ] Cancel coroutines on navigation away (verify ViewModel scopes)
- [ ] Enable R8/ProGuard for Android build, verify no runtime crashes
- [ ] Strip unused Android resources
- [ ] Measure and record APK and IPA sizes before and after optimization

## 6.4 UI/UX Polish

- [ ] Apply Material 3 color palette, typography scale, and shape system consistently
- [ ] Add loading skeleton / shimmer effects to all list screens
- [ ] Add pull-to-refresh to all list screens
- [ ] Create empty state illustrations:
  - [ ] No customers yet
  - [ ] No orders yet
  - [ ] No measurements yet
  - [ ] No tailors yet
- [ ] Create error state UI with retry buttons on all screens
- [ ] Accessibility audit:
  - [ ] Content descriptions on all images and icon buttons
  - [ ] Minimum 48dp touch targets
  - [ ] Keyboard navigation support
- [ ] Dark mode support using Material 3 dynamic theming
- [ ] Test on screen sizes: phone portrait, phone landscape, tablet

---

## Deliverables Checklist

- [ ] Image compression pipeline: resize, WEBP conversion, cached loading
- [ ] Firebase cost audit report with all optimizations applied
- [ ] Performance benchmarks meeting all targets (startup, query speed, scale)
- [ ] Polished UI with loading states, error handling, and dark mode
- [ ] App size optimized for both platforms
