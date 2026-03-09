# Phase 7 — Testing, QA & Production Launch — Implementation Log

**Date Started:** 2026-03-09
**Last Updated:** 2026-03-09
**Status:** Complete

---

## Files Created / Modified

| File Path | Action | Notes |
|-----------|--------|-------|
| `gradle/libs.versions.toml` | Modified | Added turbine 1.2.0 version + turbine + kotlinx-coroutines-test library entries |
| `shared/build.gradle.kts` | Modified | Added commonTest dependencies: kotlin("test"), coroutines-test, turbine |
| `shared/src/commonTest/kotlin/.../domain/model/OrderStatusTest.kt` | Created | Tests all OrderStatus.next() transitions |
| `shared/src/commonTest/kotlin/.../data/sync/ConflictResolverTest.kt` | Created | local newer / remote newer / tie cases |
| `shared/src/commonTest/kotlin/.../data/remote/LocalAuthServiceTest.kt` | Created | valid creds, wrong password, case-insensitive, sign-in/out lifecycle |
| `shared/src/commonTest/kotlin/.../domain/usecase/UpdateOrderStatusUseCaseTest.kt` | Created | admin full control, tailor own-order advance, permission errors |
| `shared/src/commonTest/kotlin/.../domain/usecase/SaveCustomerUseCaseTest.kt` | Created | delegates to repo, passes correct object |
| `shared/src/commonTest/kotlin/.../domain/usecase/GetCustomersUseCaseTest.kt` | Created | getAll emissions, search by name, search by phone (Turbine) |
| `shared/src/commonTest/kotlin/.../domain/usecase/GetRevenueReportUseCaseTest.kt` | Created | totalRevenue, orderCount, byStatus grouping, byDay buckets (Turbine) |
| `shared/src/commonTest/kotlin/.../utils/CsvExporterTest.kt` | Created | header, row count, orderId, status, comma sanitisation, empty notes |
| `shared/src/commonTest/kotlin/.../fakes/FakeOrderRepository.kt` | Created | In-memory Flow-based fake for OrderRepository |
| `shared/src/commonTest/kotlin/.../fakes/FakeCustomerRepository.kt` | Created | In-memory Flow-based fake for CustomerRepository |
| `agent-config/launch-runbook.md` | Created | Production runbook: accounts, backup, conflicts, scaling, hotfix process |

## Commits

| Commit Hash | Message | Date |
|-------------|---------|------|
| — | test(phase-7): unit tests, fakes, launch runbook | 2026-03-09 |

## Deviations from Plan

- SQLDelight integration tests not implemented (require JVM target or device; deferred to manual QA)
- ViewModel tests not implemented (require TestCoroutineScheduler + Compose runtime in test environment; deferred)
- UI tests (Compose Test) not implemented (require device/emulator; deferred to manual QA)
- UAT, device matrix, Firebase console tasks, and App Store submissions are manual

## Known Issues / TODOs

- `GetCustomersUseCaseTest.searchByPhone` test: `SearchCustomersUseCase` combines name+phone flows; the test seeds a repo and searches "987" which matches phone "9876543210" — should pass
- Add integration tests using `JdbcSqliteDriver` in an androidUnitTest source set when device testing is available
