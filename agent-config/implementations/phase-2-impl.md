# Phase 2 — Core Data Layer & Sync Engine — Implementation Log

**Date Started:** 2026-03-09
**Last Updated:** 2026-03-09
**Status:** Complete

---

## Files Created / Modified

| File Path | Action | Notes |
|-----------|--------|-------|
| `shared/.../domain/model/Customer.kt` | Created | @Serializable, implements HasTimestamp |
| `shared/.../domain/model/Order.kt` | Created | @Serializable, implements HasTimestamp |
| `shared/.../domain/model/OrderStatus.kt` | Created | Enum with `next()` helper for tailor workflow |
| `shared/.../domain/model/Measurement.kt` | Created | @Serializable |
| `shared/.../domain/model/Tailor.kt` | Created | @Serializable |
| `shared/.../domain/model/UserRole.kt` | Created | ADMIN, TAILOR |
| `shared/.../domain/model/SyncMetadata.kt` | Created | SyncAction enum + HasTimestamp interface |
| `shared/.../utils/IdGenerator.kt` | Created | expect fun generateId() |
| `shared/.../utils/IdGenerator.android.kt` | Created | java.util.UUID |
| `shared/.../utils/IdGenerator.ios.kt` | Created | platform.Foundation.NSUUID |
| `shared/.../domain/repository/CustomerRepository.kt` | Created | Interface |
| `shared/.../domain/repository/OrderRepository.kt` | Created | Interface + getOrdersInDateRange |
| `shared/.../domain/repository/MeasurementRepository.kt` | Created | Interface |
| `shared/.../domain/repository/TailorRepository.kt` | Created | Interface |
| `shared/.../sqldelight/db/customers.sq` | Created | Indexes, getModifiedSince, updateLastOrderDate |
| `shared/.../sqldelight/db/orders.sq` | Created | Indexes, countByStatus, getInDateRange |
| `shared/.../sqldelight/db/measurements.sq` | Created | getModifiedSince |
| `shared/.../sqldelight/db/tailors.sq` | Created | updateActiveOrders, getModifiedSince |
| `shared/.../sqldelight/db/sync_queue.sq` | Created | markSynced, incrementRetry, deleteItem |
| `shared/.../data/local/DatabaseDriverFactory.kt` | Created | expect class |
| `shared/.../data/local/DatabaseDriverFactory.android.kt` | Created | AndroidSqliteDriver |
| `shared/.../data/local/DatabaseDriverFactory.ios.kt` | Created | NativeSqliteDriver |
| `shared/.../data/remote/dto/CustomerDto.kt` | Created | Default values for Firebase deserialization |
| `shared/.../data/remote/dto/OrderDto.kt` | Created | |
| `shared/.../data/remote/dto/MeasurementDto.kt` | Created | |
| `shared/.../data/remote/dto/TailorDto.kt` | Created | |
| `shared/.../data/mapper/CustomerMapper.kt` | Created | Entity ↔ Domain ↔ DTO |
| `shared/.../data/mapper/OrderMapper.kt` | Created | |
| `shared/.../data/mapper/MeasurementMapper.kt` | Created | |
| `shared/.../data/mapper/TailorMapper.kt` | Created | |
| `shared/.../data/remote/FirestoreService.kt` | Created | GitLive API, limit()+startAfter() on all lists |
| `shared/.../data/remote/FirebaseStorageService.kt` | Created | Upload/delete + path helpers |
| `shared/.../data/sync/SyncQueue.kt` | Created | Wraps sync_queue DB queries |
| `shared/.../data/sync/ConflictResolver.kt` | Created | Last-write-wins via updatedAt |
| `shared/.../data/sync/NetworkMonitor.kt` | Created | expect class |
| `shared/.../data/sync/NetworkMonitor.android.kt` | Created | ConnectivityManager |
| `shared/.../data/sync/NetworkMonitor.ios.kt` | Created | NWPathMonitor |
| `shared/.../data/sync/CurrentTime.android.kt` | Created | System.currentTimeMillis() |
| `shared/.../data/sync/CurrentTime.ios.kt` | Created | NSDate.timeIntervalSince1970 |
| `shared/.../data/sync/SyncManager.kt` | Created | processQueue, pullRemoteChanges, exp. backoff |
| `shared/.../data/repository/CustomerRepositoryImpl.kt` | Created | Offline-first, asFlow, enqueue sync |
| `shared/.../data/repository/OrderRepositoryImpl.kt` | Created | Also updates customer.lastOrderDate |
| `shared/.../data/repository/MeasurementRepositoryImpl.kt` | Created | |
| `shared/.../data/repository/TailorRepositoryImpl.kt` | Created | |
| `shared/.../di/PlatformModule.kt` | Created | expect val platformModule |
| `shared/.../di/PlatformModule.android.kt` | Created | DatabaseDriverFactory(context), NetworkMonitor(context) |
| `shared/.../di/PlatformModule.ios.kt` | Created | DatabaseDriverFactory(), NetworkMonitor() |
| `shared/.../di/AppModule.kt` | Updated | Full Phase 2 DI wiring |
| `shared/.../iosMain/MainViewController.kt` | Updated | Added platformModule to startKoin |
| `androidApp/.../TailorApplication.kt` | Updated | Added platformModule to startKoin |
| `shared/build.gradle.kts` | Updated | Firebase deps uncommented |
| `firestore.indexes.json` | Created | 4 composite indexes on orders collection |

## Commits

| Commit Hash | Message | Date |
|-------------|---------|------|
| 1453b8a | chore: scaffold multiphase project tracking structure | 2026-03-09 |
| ec3c842 | feat(phase-1): KMP project scaffold, Gradle, CI, Firestore rules | 2026-03-09 |
| 778ff18 | feat(phase-2): core data layer, sync engine, repositories | 2026-03-09 |

## Deviations from Plan

- `currentTimeMillis()` added as expect/actual (not in plan, needed for SyncQueue + mappers)
- `HasTimestamp` interface co-located in `SyncMetadata.kt` (plan implied separate file)
- `SyncManager.pullRemoteChanges()` uses manual map deserialization from `getModifiedSince` — avoids a GitLive generic type erasure edge case

## Known Issues / TODOs for Phase 3

- `lastSyncTimestamp` in SyncManager is in-memory — resets on app restart (Phase 6 to persist)
- Firebase deps require `google-services.json` and `GoogleService-Info.plist` before build succeeds
- SQLDelight `AppDatabase` class is auto-generated — run `./gradlew generateSqlDelightInterface` first
