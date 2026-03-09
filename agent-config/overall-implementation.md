# Tailor Shop Management App — KMP Implementation Plan

**Platform:** Android + iOS (Kotlin Multiplatform)
**Architecture:** Clean Architecture + Compose Multiplatform + Firebase
**Timeline:** 18–22 Weeks | 7 Phases
**Scale Target:** 10,000 Customers · 30,000 Orders · ₹0–300/month Firebase Cost

---

## Executive Summary

This document outlines a comprehensive 7-phase implementation plan for building a cross-platform Tailor Shop Management App using Kotlin Multiplatform (KMP). The plan is structured to deliver incremental value at each phase, starting with foundational architecture and progressing through feature development, optimization, and production deployment.

The app will share approximately 90% of code between Android and iOS, using Compose Multiplatform for the UI layer, SQLDelight for offline-first local storage, and Firebase for the cloud backend (Firestore, Authentication, Storage).

---

## High-Level Timeline

| # | Phase | Focus | Duration | Team |
|---|-------|-------|----------|------|
| 1 | Foundation & Architecture | KMP setup, CI/CD, project scaffolding | 2–3 weeks | Lead + DevOps |
| 2 | Core Data Layer | Models, SQLDelight, Firebase, sync | 3–4 weeks | Backend Dev |
| 3 | Authentication & RBAC | Firebase Auth, role guards, login UI | 2 weeks | Full-stack Dev |
| 4 | Core Feature Screens | Customers, orders, measurements, tailors | 4–5 weeks | 2 Devs + QA |
| 5 | Dashboard & Analytics | KPIs, charts, search, filters | 2–3 weeks | Frontend Dev |
| 6 | Optimization & Polish | Images, performance, UX, cost tuning | 2–3 weeks | All |
| 7 | Testing & Launch | E2E tests, UAT, app store submission | 3–4 weeks | All + QA |

---

## Technology Stack

| Category | Technology |
|----------|-----------|
| Language | Kotlin (shared), Swift (iOS-specific only) |
| UI Framework | JetBrains Compose Multiplatform + Material 3 |
| Local Database | SQLDelight (type-safe, multiplatform SQL) |
| Remote Backend | Firebase: Firestore, Storage, Authentication |
| Networking | Ktor (multiplatform HTTP client) |
| Dependency Injection | Koin (lightweight, KMP-native) |
| Navigation | Voyager or Decompose (Compose Multiplatform compatible) |
| Image Loading | Coil (Android) + platform cache (iOS) |
| Serialization | kotlinx.serialization (JSON) |
| CI/CD | GitHub Actions or Bitrise |
| Testing | kotlin.test, Compose Test, Turbine (Flow testing) |
| Monitoring | Firebase Crashlytics + Performance Monitoring |

---

## Project Structure

```
root/
├── androidApp/
│   ├── src/main/
│   │   ├── AndroidManifest.xml
│   │   ├── java/.../MainActivity.kt
│   │   └── res/
│   └── build.gradle.kts
├── iosApp/
│   ├── iosApp/
│   │   ├── AppDelegate.swift
│   │   ├── ContentView.swift
│   │   └── Info.plist
│   └── iosApp.xcodeproj
├── shared/
│   ├── src/
│   │   ├── commonMain/
│   │   │   ├── kotlin/.../
│   │   │   │   ├── data/
│   │   │   │   │   ├── local/
│   │   │   │   │   │   ├── DatabaseDriverFactory.kt
│   │   │   │   │   │   ├── dao/
│   │   │   │   │   │   │   ├── CustomerDao.kt
│   │   │   │   │   │   │   ├── OrderDao.kt
│   │   │   │   │   │   │   ├── MeasurementDao.kt
│   │   │   │   │   │   │   └── TailorDao.kt
│   │   │   │   │   │   └── entity/
│   │   │   │   │   │       ├── CustomerEntity.sq
│   │   │   │   │   │       ├── OrderEntity.sq
│   │   │   │   │   │       ├── MeasurementEntity.sq
│   │   │   │   │   │       ├── TailorEntity.sq
│   │   │   │   │   │       └── SyncQueueEntity.sq
│   │   │   │   │   ├── remote/
│   │   │   │   │   │   ├── FirestoreService.kt
│   │   │   │   │   │   ├── FirebaseStorageService.kt
│   │   │   │   │   │   ├── FirebaseAuthService.kt
│   │   │   │   │   │   └── dto/
│   │   │   │   │   │       ├── CustomerDto.kt
│   │   │   │   │   │       ├── OrderDto.kt
│   │   │   │   │   │       ├── MeasurementDto.kt
│   │   │   │   │   │       └── TailorDto.kt
│   │   │   │   │   ├── repository/
│   │   │   │   │   │   ├── CustomerRepositoryImpl.kt
│   │   │   │   │   │   ├── OrderRepositoryImpl.kt
│   │   │   │   │   │   ├── MeasurementRepositoryImpl.kt
│   │   │   │   │   │   ├── TailorRepositoryImpl.kt
│   │   │   │   │   │   └── AuthRepositoryImpl.kt
│   │   │   │   │   ├── sync/
│   │   │   │   │   │   ├── SyncManager.kt
│   │   │   │   │   │   ├── SyncQueue.kt
│   │   │   │   │   │   ├── ConflictResolver.kt
│   │   │   │   │   │   └── NetworkMonitor.kt
│   │   │   │   │   └── mapper/
│   │   │   │   │       ├── CustomerMapper.kt
│   │   │   │   │       ├── OrderMapper.kt
│   │   │   │   │       ├── MeasurementMapper.kt
│   │   │   │   │       └── TailorMapper.kt
│   │   │   │   ├── domain/
│   │   │   │   │   ├── model/
│   │   │   │   │   │   ├── Customer.kt
│   │   │   │   │   │   ├── Order.kt
│   │   │   │   │   │   ├── OrderStatus.kt
│   │   │   │   │   │   ├── Measurement.kt
│   │   │   │   │   │   ├── Tailor.kt
│   │   │   │   │   │   ├── UserRole.kt
│   │   │   │   │   │   └── SyncMetadata.kt
│   │   │   │   │   ├── repository/
│   │   │   │   │   │   ├── CustomerRepository.kt
│   │   │   │   │   │   ├── OrderRepository.kt
│   │   │   │   │   │   ├── MeasurementRepository.kt
│   │   │   │   │   │   ├── TailorRepository.kt
│   │   │   │   │   │   └── AuthRepository.kt
│   │   │   │   │   └── usecase/
│   │   │   │   │       ├── customer/
│   │   │   │   │       │   ├── GetCustomersUseCase.kt
│   │   │   │   │       │   ├── GetCustomerByIdUseCase.kt
│   │   │   │   │       │   ├── AddCustomerUseCase.kt
│   │   │   │   │       │   ├── UpdateCustomerUseCase.kt
│   │   │   │   │       │   └── SearchCustomersUseCase.kt
│   │   │   │   │       ├── order/
│   │   │   │   │       │   ├── GetOrdersUseCase.kt
│   │   │   │   │       │   ├── GetOrdersByStatusUseCase.kt
│   │   │   │   │       │   ├── GetOrdersByTailorUseCase.kt
│   │   │   │   │       │   ├── CreateOrderUseCase.kt
│   │   │   │   │       │   ├── UpdateOrderStatusUseCase.kt
│   │   │   │   │       │   └── GetOrdersDueTodayUseCase.kt
│   │   │   │   │       ├── measurement/
│   │   │   │   │       │   ├── GetMeasurementsUseCase.kt
│   │   │   │   │       │   ├── AddMeasurementUseCase.kt
│   │   │   │   │       │   └── UpdateMeasurementUseCase.kt
│   │   │   │   │       ├── tailor/
│   │   │   │   │       │   ├── GetTailorsUseCase.kt
│   │   │   │   │       │   ├── AssignTailorUseCase.kt
│   │   │   │   │       │   └── GetTailorWorkloadUseCase.kt
│   │   │   │   │       ├── auth/
│   │   │   │   │       │   ├── SignInUseCase.kt
│   │   │   │   │       │   ├── SignOutUseCase.kt
│   │   │   │   │       │   └── GetCurrentUserUseCase.kt
│   │   │   │   │       └── dashboard/
│   │   │   │   │           ├── GetDashboardMetricsUseCase.kt
│   │   │   │   │           └── GetRevenueReportUseCase.kt
│   │   │   │   └── presentation/
│   │   │   │       ├── viewmodel/
│   │   │   │       │   ├── LoginViewModel.kt
│   │   │   │       │   ├── DashboardViewModel.kt
│   │   │   │       │   ├── CustomerListViewModel.kt
│   │   │   │       │   ├── CustomerDetailViewModel.kt
│   │   │   │       │   ├── AddCustomerViewModel.kt
│   │   │   │       │   ├── OrderListViewModel.kt
│   │   │   │       │   ├── CreateOrderViewModel.kt
│   │   │   │       │   ├── OrderDetailViewModel.kt
│   │   │   │       │   ├── MeasurementFormViewModel.kt
│   │   │   │       │   ├── TailorListViewModel.kt
│   │   │   │       │   └── TailorOrdersViewModel.kt
│   │   │   │       ├── state/
│   │   │   │       │   ├── LoginUiState.kt
│   │   │   │       │   ├── DashboardUiState.kt
│   │   │   │       │   ├── CustomerListUiState.kt
│   │   │   │       │   ├── OrderListUiState.kt
│   │   │   │       │   └── ...UiState.kt
│   │   │   │       ├── screen/
│   │   │   │       │   ├── LoginScreen.kt
│   │   │   │       │   ├── DashboardScreen.kt
│   │   │   │       │   ├── CustomerListScreen.kt
│   │   │   │       │   ├── CustomerDetailScreen.kt
│   │   │   │       │   ├── AddCustomerScreen.kt
│   │   │   │       │   ├── OrderListScreen.kt
│   │   │   │       │   ├── CreateOrderScreen.kt
│   │   │   │       │   ├── OrderDetailScreen.kt
│   │   │   │       │   ├── MeasurementFormScreen.kt
│   │   │   │       │   ├── TailorListScreen.kt
│   │   │   │       │   └── TailorOrdersScreen.kt
│   │   │   │       ├── component/
│   │   │   │       │   ├── KpiCard.kt
│   │   │   │       │   ├── OrderStatusChip.kt
│   │   │   │       │   ├── SearchBar.kt
│   │   │   │       │   ├── PaginatedList.kt
│   │   │   │       │   ├── ImagePicker.kt
│   │   │   │       │   ├── DatePickerField.kt
│   │   │   │       │   ├── EmptyState.kt
│   │   │   │       │   ├── LoadingSkeleton.kt
│   │   │   │       │   └── ErrorState.kt
│   │   │   │       ├── navigation/
│   │   │   │       │   ├── AppNavGraph.kt
│   │   │   │       │   ├── Screen.kt
│   │   │   │       │   └── RoleGuard.kt
│   │   │   │       └── theme/
│   │   │   │           ├── AppTheme.kt
│   │   │   │           ├── Color.kt
│   │   │   │           ├── Typography.kt
│   │   │   │           └── Shape.kt
│   │   │   └── sqldelight/
│   │   │       └── .../
│   │   │           ├── CustomerEntity.sq
│   │   │           ├── OrderEntity.sq
│   │   │           ├── MeasurementEntity.sq
│   │   │           ├── TailorEntity.sq
│   │   │           └── SyncQueueEntity.sq
│   │   ├── androidMain/
│   │   │   └── kotlin/.../
│   │   │       ├── data/local/DatabaseDriverFactory.android.kt
│   │   │       ├── data/sync/NetworkMonitor.android.kt
│   │   │       ├── data/remote/FirebaseInit.android.kt
│   │   │       └── presentation/component/ImagePicker.android.kt
│   │   └── iosMain/
│   │       └── kotlin/.../
│   │           ├── data/local/DatabaseDriverFactory.ios.kt
│   │           ├── data/sync/NetworkMonitor.ios.kt
│   │           ├── data/remote/FirebaseInit.ios.kt
│   │           └── presentation/component/ImagePicker.ios.kt
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── gradle/
    └── libs.versions.toml
```

---

## Domain Models

### Customer

```kotlin
@Serializable
data class Customer(
    val id: String,
    val name: String,
    val phone: String,
    val address: String,
    val createdAt: Long,
    val lastOrderDate: Long?,
    val updatedAt: Long
)
```

### Order

```kotlin
@Serializable
data class Order(
    val id: String,
    val customerId: String,
    val orderDate: Long,
    val deliveryDate: Long,
    val status: OrderStatus,
    val assignedTailorId: String,
    val price: Double,
    val clothImageUrl: String?,
    val designImageUrl: String?,
    val notes: String?,
    val updatedAt: Long
)

@Serializable
enum class OrderStatus {
    PENDING, IN_PROGRESS, READY, DELIVERED
}
```

### Measurement

```kotlin
@Serializable
data class Measurement(
    val id: String,
    val customerId: String,
    val shoulder: Double,
    val chest: Double,
    val waist: Double,
    val hip: Double,
    val sleeveLength: Double,
    val shirtLength: Double,
    val pantLength: Double,
    val notes: String?,
    val updatedAt: Long
)
```

### Tailor

```kotlin
@Serializable
data class Tailor(
    val id: String,
    val name: String,
    val phone: String,
    val specialization: String,
    val activeOrders: Int,
    val updatedAt: Long
)
```

### SyncMetadata

```kotlin
@Serializable
data class SyncMetadata(
    val entityId: String,
    val entityType: String,    // "customer", "order", "measurement", "tailor"
    val action: SyncAction,    // CREATE, UPDATE, DELETE
    val timestamp: Long,
    val synced: Boolean
)

@Serializable
enum class SyncAction { CREATE, UPDATE, DELETE }
```

### UserRole

```kotlin
@Serializable
enum class UserRole { ADMIN, TAILOR }
```

---

## Data Flow Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        UI LAYER                             │
│  Compose Multiplatform Screens                              │
│  (LoginScreen, DashboardScreen, OrderListScreen, etc.)      │
└──────────────────────────┬──────────────────────────────────┘
                           │ StateFlow<UiState>
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                   PRESENTATION LAYER                        │
│  ViewModels                                                 │
│  (DashboardViewModel, OrderListViewModel, etc.)             │
└──────────────────────────┬──────────────────────────────────┘
                           │ suspend fun / Flow
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                     DOMAIN LAYER                            │
│  Use Cases                                                  │
│  (GetOrdersByStatusUseCase, CreateOrderUseCase, etc.)       │
│  Repository Interfaces                                      │
│  (OrderRepository, CustomerRepository, etc.)                │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                      DATA LAYER                             │
│  ┌─────────────────┐         ┌────────────────────────┐     │
│  │  SQLDelight DB   │◄───────►│  Repository Impls     │     │
│  │  (offline-first) │         │  (read local, sync    │     │
│  └─────────────────┘         │   remote in background)│     │
│                               └───────────┬────────────┘     │
│  ┌─────────────────┐                      │                  │
│  │  Sync Engine     │◄────────────────────┘                  │
│  │  (queue, retry,  │                                        │
│  │   conflict res.) │         ┌────────────────────────┐     │
│  └────────┬─────────┘         │  Firebase Services     │     │
│           └──────────────────►│  Firestore / Storage   │     │
│                               │  Authentication        │     │
│                               └────────────────────────┘     │
└─────────────────────────────────────────────────────────────┘
```

---

---

# PHASE 1 — Foundation & Project Architecture

**Duration:** Weeks 1–3
**Team:** Lead Developer + DevOps
**Goal:** Establish the KMP project structure, configure build systems for both platforms, and set up the CI/CD pipeline. This phase produces zero user-facing features but is critical for long-term velocity and code-sharing efficiency.

---

## 1.1 KMP Project Scaffolding

- Initialize a new KMP project with the Compose Multiplatform plugin targeting Android and iOS.
- Create the three-module structure: `androidApp`, `iosApp`, and `shared`.
- Configure shared module with `data`, `domain`, and `presentation` packages following Clean Architecture.
- Set up Gradle version catalogs (`libs.versions.toml`) for centralized dependency management.
- Configure Kotlin serialization plugin for JSON handling across platforms.

### libs.versions.toml (Key Dependencies)

```toml
[versions]
kotlin = "2.0.21"
compose-multiplatform = "1.7.1"
sqldelight = "2.0.2"
ktor = "3.0.1"
koin = "4.0.0"
coroutines = "1.9.0"
firebase-kotlin = "2.1.0"

[libraries]
sqldelight-runtime = { module = "app.cash.sqldelight:runtime", version.ref = "sqldelight" }
sqldelight-coroutines = { module = "app.cash.sqldelight:coroutines-extensions", version.ref = "sqldelight" }
sqldelight-android-driver = { module = "app.cash.sqldelight:android-driver", version.ref = "sqldelight" }
sqldelight-native-driver = { module = "app.cash.sqldelight:native-driver", version.ref = "sqldelight" }
ktor-core = { module = "io.ktor:ktor-client-core", version.ref = "ktor" }
ktor-okhttp = { module = "io.ktor:ktor-client-okhttp", version.ref = "ktor" }
ktor-darwin = { module = "io.ktor:ktor-client-darwin", version.ref = "ktor" }
koin-core = { module = "io.insert-koin:koin-core", version.ref = "koin" }
koin-compose = { module = "io.insert-koin:koin-compose", version.ref = "koin" }
kotlinx-serialization = { module = "org.jetbrains.kotlinx:kotlinx-serialization-json", version = "1.7.3" }
kotlinx-coroutines = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-core", version.ref = "coroutines" }
kotlinx-datetime = { module = "org.jetbrains.kotlinx:kotlinx-datetime", version = "0.6.1" }

[plugins]
kotlin-multiplatform = { id = "org.jetbrains.kotlin.multiplatform", version.ref = "kotlin" }
compose-multiplatform = { id = "org.jetbrains.compose", version.ref = "compose-multiplatform" }
sqldelight = { id = "app.cash.sqldelight", version.ref = "sqldelight" }
kotlin-serialization = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlin" }
```

---

## 1.2 Build & CI/CD Setup

- Configure Gradle build for Android (AGP) and iOS (CocoaPods or SPM integration).
- Set up GitHub Actions pipeline with the following jobs:
  - **Lint:** Detekt + Ktlint on every PR.
  - **Build Shared:** Compile shared module (JVM + native targets).
  - **Build Android:** Assemble debug APK.
  - **Build iOS:** Build iOS framework (on macOS runner).
  - **Test:** Run shared module unit tests.
- Create debug and release build variants with environment-specific Firebase config files.

### GitHub Actions Workflow (Skeleton)

```yaml
name: CI
on: [push, pull_request]
jobs:
  lint:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - run: ./gradlew detekt ktlintCheck

  build-shared:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - run: ./gradlew :shared:compileKotlinJvm

  build-android:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - run: ./gradlew :androidApp:assembleDebug

  build-ios:
    runs-on: macos-latest
    steps:
      - uses: actions/checkout@v4
      - run: ./gradlew :shared:linkDebugFrameworkIosArm64

  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - run: ./gradlew :shared:jvmTest
```

---

## 1.3 Firebase Project Configuration

- Create a Firebase project with separate environments (dev, staging, prod).
- Register both Android and iOS apps in the Firebase console.
- Download and place `google-services.json` (Android) and `GoogleService-Info.plist` (iOS).
- Enable Firestore, Firebase Storage, and Firebase Authentication in the console.
- Draft initial Firestore security rules (start restrictive, open as needed).

### Initial Firestore Security Rules

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {

    // Default: deny all
    match /{document=**} {
      allow read, write: if false;
    }

    // Customers
    match /customers/{customerId} {
      allow read: if request.auth != null;
      allow write: if request.auth.token.role == "admin";
    }

    // Orders
    match /orders/{orderId} {
      allow read: if request.auth != null;
      allow write: if request.auth.token.role == "admin";
      allow update: if request.auth.token.role == "tailor"
                    && resource.data.assignedTailorId == request.auth.uid
                    && request.resource.data.diff(resource.data).affectedKeys().hasOnly(['status', 'updatedAt']);
    }

    // Measurements
    match /measurements/{measurementId} {
      allow read: if request.auth != null;
      allow write: if request.auth.token.role == "admin";
    }

    // Tailors
    match /tailors/{tailorId} {
      allow read: if request.auth != null;
      allow write: if request.auth.token.role == "admin";
    }
  }
}
```

---

## 1.4 Dependency Integration

- Integrate SQLDelight with platform-specific drivers (Android SQLite driver, Native SQLite driver for iOS).
- Add Ktor client with platform engines (OkHttp for Android, Darwin for iOS).
- Set up a Firebase KMP wrapper or expect/actual declarations for Firestore, Auth, and Storage.
- Integrate Koin for dependency injection across the shared module.

### Koin Module Setup (Skeleton)

```kotlin
// shared/src/commonMain/.../di/AppModule.kt

val appModule = module {
    // Database
    single { createDatabaseDriver() }  // expect/actual
    single { AppDatabase(get()) }

    // Remote Services
    single { FirestoreService() }
    single { FirebaseStorageService() }
    single { FirebaseAuthService() }

    // Repositories
    single<CustomerRepository> { CustomerRepositoryImpl(get(), get(), get()) }
    single<OrderRepository> { OrderRepositoryImpl(get(), get(), get()) }
    single<MeasurementRepository> { MeasurementRepositoryImpl(get(), get(), get()) }
    single<TailorRepository> { TailorRepositoryImpl(get(), get(), get()) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }

    // Sync
    single { SyncManager(get(), get(), get(), get(), get()) }
    single { NetworkMonitor() }  // expect/actual

    // Use Cases
    factory { GetCustomersUseCase(get()) }
    factory { CreateOrderUseCase(get(), get()) }
    factory { GetDashboardMetricsUseCase(get()) }
    // ... all other use cases

    // ViewModels
    viewModel { DashboardViewModel(get()) }
    viewModel { CustomerListViewModel(get(), get()) }
    viewModel { OrderListViewModel(get(), get()) }
    // ... all other viewmodels
}
```

---

## Phase 1 Deliverables

- [x] Compiling KMP project that runs Hello World on both Android and iOS
- [x] CI/CD pipeline running lint + build on every pull request
- [x] Firebase project configured with dev environment credentials
- [x] SQLDelight, Ktor, and DI framework integrated and verified
- [x] Documented project conventions (branching, commit format, package structure)

---

---

# PHASE 2 — Core Data Layer & Sync Engine

**Duration:** Weeks 4–7
**Team:** Backend Developer
**Goal:** Build the full data pipeline: domain models, local database, remote Firebase service, repository pattern, and the offline-first sync engine. This is the heaviest backend phase and the backbone of the entire application.

---

## 2.1 Domain Models

Define all shared domain models as Kotlin data classes with `@Serializable` annotations (see Domain Models section above). Key design decisions:

- All entities include an `updatedAt: Long` timestamp for conflict resolution.
- `OrderStatus` is an enum: `PENDING`, `IN_PROGRESS`, `READY`, `DELIVERED`.
- `SyncMetadata` tracks every local write for the offline queue.
- IDs are strings (UUIDs generated client-side) to avoid server round-trips during offline creation.

---

## 2.2 SQLDelight Local Database

### Schema Files

**customers.sq**
```sql
CREATE TABLE customers (
    id TEXT NOT NULL PRIMARY KEY,
    name TEXT NOT NULL,
    phone TEXT NOT NULL,
    address TEXT NOT NULL,
    createdAt INTEGER NOT NULL,
    lastOrderDate INTEGER,
    updatedAt INTEGER NOT NULL
);

CREATE INDEX idx_customers_name ON customers(name);
CREATE INDEX idx_customers_phone ON customers(phone);

getAll:
SELECT * FROM customers ORDER BY name ASC LIMIT :limit OFFSET :offset;

getById:
SELECT * FROM customers WHERE id = :id;

searchByName:
SELECT * FROM customers WHERE name LIKE '%' || :query || '%' ORDER BY name ASC LIMIT 20;

searchByPhone:
SELECT * FROM customers WHERE phone LIKE '%' || :query || '%' LIMIT 20;

insert:
INSERT OR REPLACE INTO customers (id, name, phone, address, createdAt, lastOrderDate, updatedAt)
VALUES (?, ?, ?, ?, ?, ?, ?);

delete:
DELETE FROM customers WHERE id = :id;

getCount:
SELECT COUNT(*) FROM customers;
```

**orders.sq**
```sql
CREATE TABLE orders (
    id TEXT NOT NULL PRIMARY KEY,
    customerId TEXT NOT NULL,
    orderDate INTEGER NOT NULL,
    deliveryDate INTEGER NOT NULL,
    status TEXT NOT NULL,
    assignedTailorId TEXT NOT NULL,
    price REAL NOT NULL,
    clothImageUrl TEXT,
    designImageUrl TEXT,
    notes TEXT,
    updatedAt INTEGER NOT NULL,
    FOREIGN KEY (customerId) REFERENCES customers(id),
    FOREIGN KEY (assignedTailorId) REFERENCES tailors(id)
);

CREATE INDEX idx_orders_customer ON orders(customerId);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_tailor ON orders(assignedTailorId);
CREATE INDEX idx_orders_delivery ON orders(deliveryDate);
CREATE INDEX idx_orders_customer_status ON orders(customerId, status);
CREATE INDEX idx_orders_tailor_delivery ON orders(assignedTailorId, deliveryDate);

getByStatus:
SELECT * FROM orders WHERE status = :status ORDER BY deliveryDate ASC LIMIT :limit OFFSET :offset;

getByCustomer:
SELECT * FROM orders WHERE customerId = :customerId ORDER BY orderDate DESC;

getByTailor:
SELECT * FROM orders WHERE assignedTailorId = :tailorId ORDER BY deliveryDate ASC;

getDueToday:
SELECT * FROM orders WHERE deliveryDate >= :startOfDay AND deliveryDate < :endOfDay;

getById:
SELECT * FROM orders WHERE id = :id;

countByStatus:
SELECT status, COUNT(*) AS count FROM orders GROUP BY status;

countByTailor:
SELECT assignedTailorId, COUNT(*) AS count FROM orders WHERE status != 'DELIVERED' GROUP BY assignedTailorId;

insert:
INSERT OR REPLACE INTO orders (id, customerId, orderDate, deliveryDate, status, assignedTailorId, price, clothImageUrl, designImageUrl, notes, updatedAt)
VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);

updateStatus:
UPDATE orders SET status = :status, updatedAt = :updatedAt WHERE id = :id;

delete:
DELETE FROM orders WHERE id = :id;
```

**measurements.sq**
```sql
CREATE TABLE measurements (
    id TEXT NOT NULL PRIMARY KEY,
    customerId TEXT NOT NULL,
    shoulder REAL NOT NULL,
    chest REAL NOT NULL,
    waist REAL NOT NULL,
    hip REAL NOT NULL,
    sleeveLength REAL NOT NULL,
    shirtLength REAL NOT NULL,
    pantLength REAL NOT NULL,
    notes TEXT,
    updatedAt INTEGER NOT NULL,
    FOREIGN KEY (customerId) REFERENCES customers(id)
);

CREATE INDEX idx_measurements_customer ON measurements(customerId);

getByCustomer:
SELECT * FROM measurements WHERE customerId = :customerId ORDER BY updatedAt DESC;

getById:
SELECT * FROM measurements WHERE id = :id;

insert:
INSERT OR REPLACE INTO measurements (id, customerId, shoulder, chest, waist, hip, sleeveLength, shirtLength, pantLength, notes, updatedAt)
VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);

delete:
DELETE FROM measurements WHERE id = :id;
```

**tailors.sq**
```sql
CREATE TABLE tailors (
    id TEXT NOT NULL PRIMARY KEY,
    name TEXT NOT NULL,
    phone TEXT NOT NULL,
    specialization TEXT NOT NULL,
    activeOrders INTEGER NOT NULL DEFAULT 0,
    updatedAt INTEGER NOT NULL
);

getAll:
SELECT * FROM tailors ORDER BY name ASC;

getById:
SELECT * FROM tailors WHERE id = :id;

insert:
INSERT OR REPLACE INTO tailors (id, name, phone, specialization, activeOrders, updatedAt)
VALUES (?, ?, ?, ?, ?, ?);

delete:
DELETE FROM tailors WHERE id = :id;
```

**sync_queue.sq**
```sql
CREATE TABLE sync_queue (
    entityId TEXT NOT NULL,
    entityType TEXT NOT NULL,
    action TEXT NOT NULL,
    timestamp INTEGER NOT NULL,
    synced INTEGER NOT NULL DEFAULT 0,
    retryCount INTEGER NOT NULL DEFAULT 0,
    PRIMARY KEY (entityId, entityType, action)
);

getPending:
SELECT * FROM sync_queue WHERE synced = 0 ORDER BY timestamp ASC;

insert:
INSERT OR REPLACE INTO sync_queue (entityId, entityType, action, timestamp, synced, retryCount)
VALUES (?, ?, ?, ?, 0, 0);

markSynced:
UPDATE sync_queue SET synced = 1 WHERE entityId = :entityId AND entityType = :entityType;

incrementRetry:
UPDATE sync_queue SET retryCount = retryCount + 1 WHERE entityId = :entityId AND entityType = :entityType;

deleteSynced:
DELETE FROM sync_queue WHERE synced = 1;

getRetryCount:
SELECT retryCount FROM sync_queue WHERE entityId = :entityId AND entityType = :entityType;
```

---

## 2.3 Firebase Remote Service

### FirestoreService

```kotlin
class FirestoreService {

    // ── Customers ──
    suspend fun getCustomers(limit: Int = 20, lastDoc: DocumentSnapshot? = null): List<CustomerDto>
    suspend fun getCustomerById(id: String): CustomerDto?
    suspend fun upsertCustomer(dto: CustomerDto)
    suspend fun deleteCustomer(id: String)

    // ── Orders ──
    suspend fun getOrdersByStatus(status: String, limit: Int = 20, lastDoc: DocumentSnapshot? = null): List<OrderDto>
    suspend fun getOrdersByCustomer(customerId: String): List<OrderDto>
    suspend fun getOrdersByTailor(tailorId: String): List<OrderDto>
    suspend fun getOrdersDueToday(): List<OrderDto>
    suspend fun upsertOrder(dto: OrderDto)
    suspend fun deleteOrder(id: String)

    // ── Measurements ──
    suspend fun getMeasurementsByCustomer(customerId: String): List<MeasurementDto>
    suspend fun upsertMeasurement(dto: MeasurementDto)

    // ── Tailors ──
    suspend fun getAllTailors(): List<TailorDto>
    suspend fun upsertTailor(dto: TailorDto)

    // ── Sync ──
    suspend fun getModifiedSince(collection: String, timestamp: Long): List<Map<String, Any>>
}
```

**Critical rules enforced in every query:**
- Never fetch an entire collection without a filter.
- Always apply `limit()` for list queries (default 20).
- Use `startAfter(lastDoc)` for pagination.
- Use composite queries where available.

### Firestore Composite Indexes Required

| Collection | Fields | Purpose |
|-----------|--------|---------|
| orders | `customerId` + `status` | Filter orders by customer and status |
| orders | `assignedTailorId` + `deliveryDate` | Tailor's upcoming orders sorted by due date |
| orders | `status` + `deliveryDate` | Dashboard: pending orders sorted by urgency |
| orders | `status` + `orderDate` | Revenue reports by date within status |

---

## 2.4 Repository Pattern

### Interface (Domain Layer)

```kotlin
interface OrderRepository {
    fun getOrdersByStatus(status: OrderStatus): Flow<List<Order>>
    fun getOrdersByCustomer(customerId: String): Flow<List<Order>>
    fun getOrdersByTailor(tailorId: String): Flow<List<Order>>
    fun getOrdersDueToday(): Flow<List<Order>>
    fun getOrderById(id: String): Flow<Order?>
    fun getDashboardCounts(): Flow<Map<OrderStatus, Int>>
    suspend fun createOrder(order: Order)
    suspend fun updateOrderStatus(orderId: String, status: OrderStatus)
    suspend fun deleteOrder(orderId: String)
}
```

### Implementation (Data Layer)

```kotlin
class OrderRepositoryImpl(
    private val db: AppDatabase,
    private val firestore: FirestoreService,
    private val syncQueue: SyncQueue
) : OrderRepository {

    override fun getOrdersByStatus(status: OrderStatus): Flow<List<Order>> {
        // Read from local DB (instant, works offline)
        return db.orderQueries.getByStatus(status.name, limit = 20, offset = 0)
            .asFlow()
            .mapToList()
            .map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun createOrder(order: Order) {
        // 1. Write to local DB immediately
        db.orderQueries.insert(order.toEntity())

        // 2. Enqueue for remote sync
        syncQueue.enqueue(
            entityId = order.id,
            entityType = "order",
            action = SyncAction.CREATE
        )
    }

    // All other methods follow the same pattern:
    // READ  → local DB first (Flow for reactivity)
    // WRITE → local DB + enqueue sync
}
```

---

## 2.5 Offline Sync Engine

### SyncManager

```kotlin
class SyncManager(
    private val db: AppDatabase,
    private val firestore: FirestoreService,
    private val networkMonitor: NetworkMonitor,
    private val conflictResolver: ConflictResolver,
    private val scope: CoroutineScope
) {
    private val maxRetries = 5

    fun startSync() {
        scope.launch {
            networkMonitor.isOnline.collect { isOnline ->
                if (isOnline) processQueue()
            }
        }
    }

    private suspend fun processQueue() {
        val pending = db.syncQueueQueries.getPending().executeAsList()

        for (item in pending) {
            try {
                when (item.entityType) {
                    "customer" -> syncCustomer(item)
                    "order"    -> syncOrder(item)
                    "measurement" -> syncMeasurement(item)
                    "tailor"   -> syncTailor(item)
                }
                db.syncQueueQueries.markSynced(item.entityId, item.entityType)
            } catch (e: Exception) {
                handleSyncError(item, e)
            }
        }

        // Clean up synced items
        db.syncQueueQueries.deleteSynced()

        // Pull remote changes
        pullRemoteChanges()
    }

    private suspend fun pullRemoteChanges() {
        // For each collection, fetch documents modified since last sync
        val lastSync = getLastSyncTimestamp()
        // Pull and merge into local DB with conflict resolution
    }

    private suspend fun handleSyncError(item: SyncQueueEntity, error: Exception) {
        val retryCount = item.retryCount
        if (retryCount < maxRetries) {
            db.syncQueueQueries.incrementRetry(item.entityId, item.entityType)
            // Exponential backoff: 1s, 2s, 4s, 8s, 16s
            delay((1000L * (1 shl retryCount)).coerceAtMost(30_000L))
        } else {
            // Log permanently failed sync for admin review
            logSyncFailure(item, error)
        }
    }
}
```

### ConflictResolver

```kotlin
class ConflictResolver {
    fun resolve(local: Any, remote: Any): Any {
        // Compare updatedAt timestamps — latest wins
        val localTimestamp = (local as? HasTimestamp)?.updatedAt ?: 0L
        val remoteTimestamp = (remote as? HasTimestamp)?.updatedAt ?: 0L

        return if (localTimestamp >= remoteTimestamp) local else remote
    }
}

interface HasTimestamp {
    val updatedAt: Long
}
```

### NetworkMonitor (expect/actual)

```kotlin
// commonMain
expect class NetworkMonitor() {
    val isOnline: StateFlow<Boolean>
}

// androidMain
actual class NetworkMonitor {
    actual val isOnline: StateFlow<Boolean>
    // Uses ConnectivityManager
}

// iosMain
actual class NetworkMonitor {
    actual val isOnline: StateFlow<Boolean>
    // Uses NWPathMonitor
}
```

---

## Phase 2 Deliverables

- [x] All domain models defined with serialization support
- [x] SQLDelight schema with all tables, indexes, and typed queries
- [x] FirestoreService with paginated, filtered queries for all entities
- [x] Repository implementations with local-first reads and background sync
- [x] Offline sync engine with queue, conflict resolution, and retry logic
- [x] Unit tests for database queries, repository logic, and sync scenarios

---

---

# PHASE 3 — Authentication & Role-Based Access

**Duration:** Weeks 8–9
**Team:** Full-stack Developer
**Goal:** Implement Firebase Authentication with role-based access control, ensuring Admins and Tailors have appropriate permissions throughout the app.

---

## 3.1 Firebase Auth Integration

- Implement email/password sign-in using Firebase Authentication SDK (expect/actual for platform-specific code).
- Create an `AuthRepository` in the shared module exposing:

```kotlin
interface AuthRepository {
    val currentUser: StateFlow<AppUser?>
    val isAuthenticated: StateFlow<Boolean>
    suspend fun signIn(email: String, password: String): Result<AppUser>
    suspend fun signOut()
    fun observeAuthState(): Flow<AppUser?>
}

data class AppUser(
    val uid: String,
    val email: String,
    val role: UserRole,
    val displayName: String?
)
```

- Store the auth token locally and refresh automatically. Handle token expiration gracefully.
- Implement session management: secure token storage using Android Keystore / iOS Keychain.

---

## 3.2 Role Management

- Define roles using Firestore custom claims: `Admin` and `Tailor`.
- Create a Cloud Function to assign roles:

```javascript
// Firebase Cloud Function
exports.setUserRole = functions.https.onCall(async (data, context) => {
    // Only admins can set roles
    if (context.auth.token.role !== 'admin') {
        throw new functions.https.HttpsError('permission-denied', 'Only admins can assign roles.');
    }

    const { uid, role } = data;
    await admin.auth().setCustomUserClaims(uid, { role });
    return { message: `Role ${role} assigned to user ${uid}` };
});
```

- On login, decode the JWT token to extract the role claim and store it in app state.
- Build a `RoleGuard` utility:

```kotlin
class RoleGuard(private val authRepository: AuthRepository) {
    fun requireAdmin(): Boolean {
        return authRepository.currentUser.value?.role == UserRole.ADMIN
    }

    fun requireTailor(): Boolean {
        return authRepository.currentUser.value?.role == UserRole.TAILOR
    }

    fun canModifyOrder(order: Order): Boolean {
        val user = authRepository.currentUser.value ?: return false
        return when (user.role) {
            UserRole.ADMIN -> true
            UserRole.TAILOR -> order.assignedTailorId == user.uid
        }
    }
}
```

---

## 3.3 Firestore Security Rules

Deploy the granular security rules defined in Phase 1.3, then test with role-specific accounts:

| Action | Admin | Tailor |
|--------|-------|--------|
| Read all customers | ✅ | ✅ |
| Create/edit customers | ✅ | ❌ |
| Read all orders | ✅ | ✅ (own only recommended) |
| Create/edit orders | ✅ | ❌ |
| Update order status (own) | ✅ | ✅ |
| Upload images | ✅ | ❌ |
| Manage tailors | ✅ | ❌ |

---

## 3.4 Login UI

- Build the Login Screen using Compose Multiplatform with Material 3 theming.
- Include form validation: email format, minimum password length, error messages.
- Show role-specific dashboard after login.
- Handle edge cases: wrong credentials, network error during login, account disabled.

```kotlin
@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Logo / Title
        Text("Tailor Shop", style = MaterialTheme.typography.headlineLarge)

        Spacer(Modifier.height(32.dp))

        // Email Field
        OutlinedTextField(
            value = state.email,
            onValueChange = viewModel::onEmailChanged,
            label = { Text("Email") },
            isError = state.emailError != null,
            supportingText = state.emailError?.let { { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        // Password Field
        OutlinedTextField(
            value = state.password,
            onValueChange = viewModel::onPasswordChanged,
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            isError = state.passwordError != null,
            supportingText = state.passwordError?.let { { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        // Login Button
        Button(
            onClick = viewModel::onSignIn,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.isLoading) CircularProgressIndicator(Modifier.size(20.dp))
            else Text("Sign In")
        }

        // Error Banner
        state.error?.let {
            Spacer(Modifier.height(16.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }
    }
}
```

---

## Phase 3 Deliverables

- [x] Firebase Auth integration with email/password sign-in on both platforms
- [x] Role-based access control with Admin and Tailor roles
- [x] Firestore security rules deployed and tested
- [x] Login screen with validation and error handling
- [x] Auth state management with auto-refresh and secure storage

---

---

# PHASE 4 — Core Feature Screens

**Duration:** Weeks 10–14
**Team:** 2 Developers + QA
**Goal:** Build all primary screens using Compose Multiplatform. Each screen connects to its ViewModel, which calls use cases backed by the repository layer built in Phase 2.

---

## 4.1 Customer Management

**Customer List Screen:**
- `LazyColumn` with search bar, paginated loading (20 items per page), pull-to-refresh.
- Each item shows name, phone, last order date.
- Tap navigates to Customer Detail.
- FAB to add new customer (Admin only).

**Add/Edit Customer Screen:**
- Form with fields for name, phone (with format validation), and address.
- Save writes to SQLDelight and enqueues sync.
- Input validation with real-time feedback.

**Customer Detail Screen:**
- Customer info header.
- Measurement summary card (latest measurement values).
- Order history list (most recent first).
- Action buttons: Add Measurement, Create Order, Edit Customer.

---

## 4.2 Order Management

**Order List Screen:**
- Filterable by status chips: Pending, In Progress, Ready, Delivered.
- Secondary filters: assigned tailor, date range.
- Paginated with `limit(20)` + load more.

**Create Order Screen:**
- Select customer (searchable dropdown).
- Select tailor (dropdown showing workload).
- Set delivery date (date picker).
- Enter price.
- Upload cloth image and design reference image.
- Notes text field.

**Order Detail Screen:**
- Full order info with status timeline visualization.
- Customer info section (tap to navigate).
- Tailor info section.
- Images displayed from Firebase Storage URLs (cached locally).
- Action buttons based on role:
  - Admin: Update Status, Reassign Tailor, Edit, Delete.
  - Tailor: Advance Status (next step only).

**Order Status Workflow:**

```
PENDING ──► IN_PROGRESS ──► READY ──► DELIVERED
   │              │            │
   └──────────────┴────────────┘
         Admin can set any status
         Tailor can only advance to next
```

---

## 4.3 Measurement Module

**Measurement Form:**
- Numeric input fields with labeled units (cm/inches toggle):
  - Shoulder, Chest, Waist, Hip
  - Sleeve Length, Shirt Length, Pant Length
- Free-text notes field.
- Input validation: reasonable ranges (e.g., shoulder 10–60 cm).

**Measurement History:**
- On Customer Detail screen, show list of past measurements with timestamps.
- Tap to view full measurement details.
- Support multiple measurements per customer.

---

## 4.4 Tailor Management

**Tailor List Screen (Admin only):**
- Shows all tailors with name, specialization, active order count.
- Tap to view tailor's assigned orders.
- Add/edit tailor profiles.

**Tailor Orders Screen:**
- Primary screen for the Tailor role.
- Filtered view showing only orders assigned to the logged-in tailor.
- Grouped by status with counts.
- Quick action: tap to advance order status.

---

## 4.5 Navigation Architecture

Using Voyager (or Decompose) for shared navigation:

```kotlin
sealed class Screen {
    object Login : Screen()
    object Dashboard : Screen()
    object CustomerList : Screen()
    data class CustomerDetail(val customerId: String) : Screen()
    object AddCustomer : Screen()
    object OrderList : Screen()
    object CreateOrder : Screen()
    data class OrderDetail(val orderId: String) : Screen()
    data class MeasurementForm(val customerId: String) : Screen()
    object TailorList : Screen()
    data class TailorOrders(val tailorId: String) : Screen()
}
```

**Navigation rules:**
- Admin sees: Dashboard, Customers, Orders, Tailors, all detail screens.
- Tailor sees: Dashboard (limited), Tailor Orders (own), Order Detail (own).
- Unauthenticated: Login screen only.
- Deep linking: `app://orders/{orderId}`, `app://customers/{customerId}`.

---

## Screen ↔ ViewModel ↔ UseCase Mapping

| Screen | ViewModel | Primary Use Cases |
|--------|-----------|-------------------|
| Login | LoginViewModel | SignInUseCase |
| Dashboard | DashboardViewModel | GetDashboardMetricsUseCase |
| Customer List | CustomerListViewModel | GetCustomersUseCase, SearchCustomersUseCase |
| Customer Detail | CustomerDetailViewModel | GetCustomerByIdUseCase, GetMeasurementsUseCase, GetOrdersByCustomerUseCase |
| Add Customer | AddCustomerViewModel | AddCustomerUseCase |
| Order List | OrderListViewModel | GetOrdersByStatusUseCase |
| Create Order | CreateOrderViewModel | CreateOrderUseCase, GetCustomersUseCase, GetTailorsUseCase |
| Order Detail | OrderDetailViewModel | GetOrderByIdUseCase, UpdateOrderStatusUseCase |
| Measurement Form | MeasurementFormViewModel | AddMeasurementUseCase |
| Tailor List | TailorListViewModel | GetTailorsUseCase, GetTailorWorkloadUseCase |
| Tailor Orders | TailorOrdersViewModel | GetOrdersByTailorUseCase |

---

## Phase 4 Deliverables

- [x] Customer list, add/edit, and detail screens fully functional
- [x] Order list, create, and detail screens with status workflow
- [x] Measurement form linked to customers with history view
- [x] Tailor list and tailor-specific order views
- [x] Shared navigation graph with role-based routing
- [x] All screens connected to ViewModels with StateFlow-driven UI

---

---

# PHASE 5 — Dashboard, Analytics & Search

**Duration:** Weeks 15–17
**Team:** Frontend Developer
**Goal:** Build the admin dashboard with real-time KPIs, and add cross-entity search and advanced filtering.

---

## 5.1 Dashboard KPI Cards

The dashboard displays four primary metrics, each as a Material 3 card component:

| Metric | Query | Source |
|--------|-------|--------|
| Orders Today | `orderDate == today` | SQLDelight `getDueToday()` |
| Pending Orders | `status == PENDING` | SQLDelight `countByStatus()` |
| Ready for Delivery | `status == READY` | SQLDelight `countByStatus()` |
| Orders Per Tailor | Grouped count | SQLDelight `countByTailor()` |

All metrics computed from local SQLDelight database for instant rendering. Background sync ensures data freshness.

```kotlin
data class DashboardUiState(
    val ordersToday: Int = 0,
    val pendingOrders: Int = 0,
    val readyOrders: Int = 0,
    val tailorWorkload: List<TailorWorkload> = emptyList(),
    val isLoading: Boolean = true,
    val lastSyncTime: String = ""
)

data class TailorWorkload(
    val tailorName: String,
    val activeOrderCount: Int
)
```

---

## 5.2 Advanced Search & Filters

- **Global search:** Across customers (name or phone) and orders (ID or customer name).
- **Date range picker:** Filter orders by delivery date range (e.g., orders due this week).
- **Multi-status filter chips:** Toggle Pending, In Progress, Ready, Delivered independently.
- **Implementation:** All search runs locally on SQLDelight with `LIKE` queries for instant results.

---

## 5.3 Reporting & Export

- **Daily/weekly summary:** Order totals and status breakdown by time period.
- **Revenue tracking:** Sum of order prices grouped by day, week, or month.
- **CSV export:** Generate a CSV file from order data for accounting.

```kotlin
class GetRevenueReportUseCase(private val orderRepository: OrderRepository) {
    operator fun invoke(startDate: Long, endDate: Long): Flow<RevenueReport> {
        return orderRepository.getOrdersInDateRange(startDate, endDate)
            .map { orders ->
                RevenueReport(
                    totalRevenue = orders.sumOf { it.price },
                    orderCount = orders.size,
                    byStatus = orders.groupBy { it.status }
                        .mapValues { (_, list) -> list.sumOf { it.price } },
                    byDay = orders.groupBy { it.orderDate.toDayString() }
                        .mapValues { (_, list) -> list.sumOf { it.price } }
                )
            }
    }
}
```

---

## Phase 5 Deliverables

- [x] Dashboard screen with four KPI cards rendering from local data
- [x] Global search across customers and orders
- [x] Advanced filter UI with date range and multi-status selection
- [x] Revenue and order summary reports
- [x] Optional CSV export functionality

---

---

# PHASE 6 — Optimization & Polish

**Duration:** Weeks 18–20
**Team:** All developers
**Goal:** Optimize image handling, tune Firebase costs, polish the UI for production quality, and ensure the system performs well at scale.

---

## 6.1 Image Optimization Pipeline

| Step | Detail |
|------|--------|
| Resize | Max 1024px on the longest edge |
| Format | Convert to WEBP |
| Target size | 120–150 KB |
| Storage path | `orders/{orderId}/cloth.webp`, `orders/{orderId}/design.webp` |
| Firestore | Store download URL only (never embed base64) |
| Caching | On-device cache via Coil (Android) / platform cache (iOS) |

```kotlin
// expect/actual for image compression
expect class ImageCompressor() {
    suspend fun compress(imageBytes: ByteArray): ByteArray
    // Returns WEBP bytes, max 1024px, ~120-150KB
}
```

---

## 6.2 Firebase Cost Optimization

**Target:** ₹0–300/month for a typical tailor shop.

**Optimization checklist:**

| Check | Action |
|-------|--------|
| No full-collection reads | Audit every Firestore query — all must have filters |
| Pagination everywhere | Every list query uses `limit(20)` + `startAfter()` |
| Local-first reads | UI reads from SQLDelight; Firestore is for sync only |
| Delta sync | Use `lastSyncTimestamp` per collection — fetch only modified docs |
| Billing alerts | Set alerts at ₹100 and ₹200 in Firebase console |
| Read estimation | ~50 active reads/day × 30 days = 1,500 reads/month (well within free tier) |

---

## 6.3 Performance Tuning

| Metric | Target |
|--------|--------|
| App startup to interactive | < 2 seconds |
| SQLDelight query response (10K records) | < 100ms |
| List scroll (30K orders) | 60fps with LazyColumn |
| Sync queue processing | < 5 seconds for 50 pending items |
| APK size | < 15 MB |
| IPA size | < 25 MB |

**Actions:**
- Profile startup with Android Profiler / Xcode Instruments.
- Use Compose `LazyColumn` with stable keys for efficient recomposition.
- Load-test SQLDelight with 10K customer + 30K order records.
- Optimize coroutine scopes: cancel work on navigation away.
- Enable R8/ProGuard for Android; strip unused resources.

---

## 6.4 UI/UX Polish

- Material 3 theming: consistent color palette, typography scale, shape system.
- Loading skeletons and shimmer effects for list screens.
- Pull-to-refresh on all list screens.
- Empty state illustrations (no customers yet, no orders, etc.).
- Error state UI with retry buttons.
- Accessibility: content descriptions, 48dp minimum touch targets, keyboard navigation.
- Dark mode support using Material 3 dynamic theming.

---

## Phase 6 Deliverables

- [x] Image compression pipeline: resize, WEBP conversion, cached loading
- [x] Firebase cost audit report with all optimizations applied
- [x] Performance benchmarks meeting all targets (startup, query speed, scale)
- [x] Polished UI with loading states, error handling, and dark mode
- [x] App size optimized for both platforms

---

---

# PHASE 7 — Testing, QA & Production Launch

**Duration:** Weeks 21–24
**Team:** All developers + QA
**Goal:** Execute comprehensive testing, conduct user acceptance testing, and deploy to app stores.

---

## 7.1 Automated Testing

| Layer | Type | Tool | Target |
|-------|------|------|--------|
| Domain | Unit tests | kotlin.test | All use cases, mappers |
| Data | Integration tests | SQLDelight in-memory driver | All DB queries |
| Data | Sync tests | kotlin.test + Turbine | Queue processing, conflict resolution |
| Presentation | ViewModel tests | kotlin.test + Turbine | State transitions, error handling |
| UI | Compose tests | Compose Test framework | Critical flows (login, create order, status update) |

**Coverage target:** Minimum 80% on the shared module.

**Critical test scenarios:**
- Create order while offline → go online → verify sync.
- Two devices edit same order → verify conflict resolution.
- Tailor tries to access admin functions → verify denial.
- Pagination: load 500 orders in 25 pages → verify no duplicates.
- Image upload fails → retry → verify eventual consistency.

---

## 7.2 Manual QA & User Acceptance Testing

- **Device matrix:**
  - Android: Low-end (2GB RAM), Mid-range, Flagship
  - iOS: iPhone SE, iPhone 15, iPad
- **QA test plan:** All screens × both roles × online and offline states.
- **UAT:** 2–3 actual tailor shop users test real workflows.
  - Observe: Where do they hesitate? What's confusing?
  - Collect: Feature requests, pain points, workflow mismatches.
- **Offline testing:** Create orders while offline, reconnect, verify everything syncs.
- **Security testing:** Verify Firestore rules with role-specific test accounts.

---

## 7.3 Pre-Launch Checklist

- [ ] Switch Firebase to production environment with billing configured
- [ ] Enable Firestore backups (scheduled daily)
- [ ] Set up Firebase Crashlytics for crash reporting on both platforms
- [ ] Configure Firebase Performance Monitoring
- [ ] Prepare app store assets:
  - App icon (all required sizes)
  - Screenshots (phone + tablet)
  - Feature graphic (Android)
  - App description, keywords
  - Privacy policy URL
- [ ] Android: generate signed release AAB, upload to Play Console (internal track first)
- [ ] iOS: archive with proper provisioning profiles, upload to App Store Connect (TestFlight first)
- [ ] Verify analytics events are firing correctly
- [ ] Run final security audit on Firestore rules

---

## 7.4 Deployment & Post-Launch

**Staged rollout:**

| Stage | Platform | Audience | Duration |
|-------|----------|----------|----------|
| Internal | Both | Dev team only | 3 days |
| Beta | Android (Internal Track) / iOS (TestFlight) | 5–10 testers | 1 week |
| Production | Google Play + App Store | Public | Ongoing |

**Post-launch monitoring (first 72 hours):**
- Crashlytics dashboard: zero unhandled crashes target.
- Performance monitoring: startup time, network latency.
- Firestore usage: verify read/write counts are within estimates.
- User feedback: in-app feedback mechanism or direct contact.

**Hotfix pipeline:**
- Branch from `main`, fix, test, release.
- Android: staged rollout (10% → 50% → 100%).
- iOS: expedited review if critical.

**Operational runbook:**
- Adding new tailors and user accounts.
- Resetting passwords.
- Exporting data for backup.
- Handling sync conflicts manually.
- Scaling Firebase if user base grows.

---

## Phase 7 Deliverables

- [x] Full test suite: unit, integration, UI, and sync engine tests
- [x] QA sign-off and UAT feedback incorporated
- [x] App published on Google Play Store and Apple App Store
- [x] Crashlytics and performance monitoring active
- [x] Operational runbook and post-launch monitoring plan

---

---

# Risk Matrix & Mitigation

| Risk | Likelihood | Impact | Mitigation |
|------|-----------|--------|------------|
| iOS-specific Firebase SDK issues | Medium | High | Use expect/actual abstractions; maintain REST API fallback via Ktor |
| Firestore cost spikes from unfiltered queries | Medium | Medium | Enforce query audits in code review; billing alerts at ₹200/month |
| Offline sync data conflicts | Low | High | Timestamp-based resolution with conflict log; admin manual override |
| Image upload failures on poor networks | Medium | Low | Retry with exponential backoff; queue persists across app restarts |
| App store rejection (iOS) | Low | High | Follow Apple HIG; test on all target devices; prepare for review feedback |
| Performance degradation at scale | Low | Medium | Load test with 10K+ records during Phase 6; index optimization |
| KMP library version incompatibilities | Medium | Medium | Pin versions in version catalog; test upgrades in isolated branches |
| Scope creep during Phase 4 (feature screens) | High | Medium | Strict sprint planning; defer nice-to-haves to post-launch backlog |

---

# Success Criteria

| Criteria | Target |
|----------|--------|
| Code sharing between platforms | ≥ 90% |
| App startup time | < 2 seconds |
| Offline operation | Full CRUD without network |
| Sync reliability | 99.9% eventual consistency |
| Firebase monthly cost | ₹0–300 |
| Crash-free rate | > 99.5% |
| User satisfaction (UAT) | > 4/5 average rating |
| Test coverage (shared module) | ≥ 80% |
| Scale support | 10K customers, 30K orders |

---

*End of Implementation Plan*