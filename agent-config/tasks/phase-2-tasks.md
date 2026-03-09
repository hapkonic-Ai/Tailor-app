# Phase 2 — Core Data Layer & Sync Engine Tasks

**Duration:** Weeks 4–7
**Team:** Backend Developer
**Status:** Not Started
**Started:** —
**Completed:** —

---

## 2.1 Domain Models

- [ ] Define `Customer` data class with `@Serializable`
- [ ] Define `Order` data class with `@Serializable`
- [ ] Define `OrderStatus` enum (`PENDING`, `IN_PROGRESS`, `READY`, `DELIVERED`)
- [ ] Define `Measurement` data class with `@Serializable`
- [ ] Define `Tailor` data class with `@Serializable`
- [ ] Define `SyncMetadata` data class with `@Serializable`
- [ ] Define `SyncAction` enum (`CREATE`, `UPDATE`, `DELETE`)
- [ ] Define `UserRole` enum (`ADMIN`, `TAILOR`)
- [ ] Define `HasTimestamp` interface for conflict resolution

## 2.2 SQLDelight Local Database

- [ ] Create `customers.sq` schema with indexes and all queries (`getAll`, `getById`, `searchByName`, `searchByPhone`, `insert`, `delete`, `getCount`)
- [ ] Create `orders.sq` schema with indexes and all queries (`getByStatus`, `getByCustomer`, `getByTailor`, `getDueToday`, `getById`, `countByStatus`, `countByTailor`, `insert`, `updateStatus`, `delete`)
- [ ] Create `measurements.sq` schema with indexes and all queries (`getByCustomer`, `getById`, `insert`, `delete`)
- [ ] Create `tailors.sq` schema with all queries (`getAll`, `getById`, `insert`, `delete`)
- [ ] Create `sync_queue.sq` schema with all queries (`getPending`, `insert`, `markSynced`, `incrementRetry`, `deleteSynced`, `getRetryCount`)
- [ ] Create `DatabaseDriverFactory.kt` (expect/actual for Android + iOS)
- [ ] Create `AppDatabase` wrapper class
- [ ] Write integration tests for all DB queries using in-memory SQLDelight driver

## 2.3 Data Transfer Objects (DTOs)

- [ ] Create `CustomerDto.kt`
- [ ] Create `OrderDto.kt`
- [ ] Create `MeasurementDto.kt`
- [ ] Create `TailorDto.kt`

## 2.4 Mappers

- [ ] Create `CustomerMapper.kt` (Entity ↔ Domain ↔ DTO)
- [ ] Create `OrderMapper.kt`
- [ ] Create `MeasurementMapper.kt`
- [ ] Create `TailorMapper.kt`

## 2.5 Firebase Remote Service

- [ ] Implement `FirestoreService.kt` with all methods:
  - [ ] Customer: `getCustomers`, `getCustomerById`, `upsertCustomer`, `deleteCustomer`
  - [ ] Order: `getOrdersByStatus`, `getOrdersByCustomer`, `getOrdersByTailor`, `getOrdersDueToday`, `upsertOrder`, `deleteOrder`
  - [ ] Measurement: `getMeasurementsByCustomer`, `upsertMeasurement`
  - [ ] Tailor: `getAllTailors`, `upsertTailor`
  - [ ] Sync: `getModifiedSince`
- [ ] Implement `FirebaseStorageService.kt` (upload/download image URLs)
- [ ] Enforce: every list query has `limit(20)` + `startAfter()` pagination
- [ ] Create required Firestore composite indexes (4 indexes on `orders` collection)

## 2.6 Repository Interfaces (Domain Layer)

- [ ] Create `CustomerRepository.kt` interface
- [ ] Create `OrderRepository.kt` interface
- [ ] Create `MeasurementRepository.kt` interface
- [ ] Create `TailorRepository.kt` interface

## 2.7 Repository Implementations (Data Layer)

- [ ] Implement `CustomerRepositoryImpl.kt` (local-first reads, write + enqueue sync)
- [ ] Implement `OrderRepositoryImpl.kt`
- [ ] Implement `MeasurementRepositoryImpl.kt`
- [ ] Implement `TailorRepositoryImpl.kt`

## 2.8 Offline Sync Engine

- [ ] Create `SyncQueue.kt` (enqueue writes to `sync_queue` table)
- [ ] Create `NetworkMonitor.kt` (expect/actual — `ConnectivityManager` on Android, `NWPathMonitor` on iOS)
- [ ] Create `ConflictResolver.kt` (last-write-wins via `updatedAt` timestamp)
- [ ] Implement `SyncManager.kt`:
  - [ ] `startSync()` — observe network, trigger `processQueue()` on reconnect
  - [ ] `processQueue()` — iterate pending items, call Firestore, mark synced
  - [ ] `pullRemoteChanges()` — delta sync via `lastSyncTimestamp`
  - [ ] `handleSyncError()` — exponential backoff (1s → 2s → 4s → 8s → 16s), max 5 retries
  - [ ] Log permanently failed syncs for admin review

## 2.9 Koin DI Update

- [ ] Register all repositories, services, SyncManager in `AppModule.kt`

## 2.10 Tests

- [ ] Unit tests: all domain mappers
- [ ] Integration tests: all SQLDelight queries
- [ ] Unit tests: `ConflictResolver` — latest timestamp wins
- [ ] Unit tests: `SyncManager` — queue processing, retry logic, error handling
- [ ] Unit tests: offline create → online → verify sync flow

---

## Deliverables Checklist

- [ ] All domain models defined with serialization support
- [ ] SQLDelight schema with all tables, indexes, and typed queries
- [ ] FirestoreService with paginated, filtered queries for all entities
- [ ] Repository implementations with local-first reads and background sync
- [ ] Offline sync engine with queue, conflict resolution, and retry logic
- [ ] Unit tests for database queries, repository logic, and sync scenarios
