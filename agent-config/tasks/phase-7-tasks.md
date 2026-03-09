# Phase 7 — Testing, QA & Production Launch Tasks

**Duration:** Weeks 21–24
**Team:** All developers + QA
**Status:** Not Started
**Started:** —
**Completed:** —

---

## 7.1 Automated Testing

### Unit Tests (kotlin.test)
- [ ] All use cases (domain layer) — happy path + error cases
- [ ] All mappers (entity ↔ domain ↔ DTO)
- [ ] `ConflictResolver` — timestamp-based resolution
- [ ] `SyncManager` — queue processing, retry logic

### Integration Tests (SQLDelight in-memory driver)
- [ ] All DB queries for customers, orders, measurements, tailors, sync_queue
- [ ] Paginated queries return correct pages
- [ ] Index usage verified by query performance

### Sync Tests (kotlin.test + Turbine)
- [ ] Offline create order → go online → verify item synced to Firestore
- [ ] Two devices edit same order → conflict resolved by `updatedAt` (latest wins)
- [ ] Retry logic: 5 retries with exponential backoff, then logs failure
- [ ] Pagination: load 500 orders in 25 pages, verify no duplicates

### ViewModel Tests (kotlin.test + Turbine)
- [ ] `LoginViewModel` — sign-in success/failure state transitions
- [ ] `OrderListViewModel` — filter, pagination state
- [ ] `CustomerListViewModel` — search state transitions
- [ ] `DashboardViewModel` — metrics load from DB

### UI Tests (Compose Test framework)
- [ ] Login flow: enter credentials → sign in → land on correct screen per role
- [ ] Create order: fill form → submit → order appears in list
- [ ] Update order status: tailor advances status → UI reflects new status
- [ ] Search customer: type name → filtered list appears

### Coverage
- [ ] Achieve ≥ 80% test coverage on shared module
- [ ] Generate coverage report and log in [phase-7-impl.md](../implementations/phase-7-impl.md)

## 7.2 Manual QA & User Acceptance Testing

### Device Matrix
- [ ] Android low-end (2GB RAM) — test all critical flows
- [ ] Android mid-range — test all critical flows
- [ ] Android flagship — test all critical flows
- [ ] iPhone SE — test all critical flows
- [ ] iPhone 15 — test all critical flows
- [ ] iPad — test all critical flows

### QA Test Plan
- [ ] All screens × both roles × online state
- [ ] All screens × both roles × offline state
- [ ] Reconnect after offline: verify sync completes
- [ ] Security: Admin role has full access
- [ ] Security: Tailor role cannot access admin screens
- [ ] Security: Tailor can only update status of own orders

### User Acceptance Testing (UAT)
- [ ] Recruit 2–3 actual tailor shop users
- [ ] Conduct UAT sessions, observe workflows
- [ ] Document: where users hesitate, what's confusing
- [ ] Collect feature requests and pain points
- [ ] Incorporate critical UAT feedback before launch

## 7.3 Pre-Launch Checklist

- [ ] Switch Firebase to production environment with billing configured
- [ ] Enable Firestore daily scheduled backups
- [ ] Set up Firebase Crashlytics on both Android and iOS
- [ ] Configure Firebase Performance Monitoring
- [ ] Prepare app store assets:
  - [ ] App icon (all required sizes for Android + iOS)
  - [ ] Screenshots (phone + tablet for both platforms)
  - [ ] Feature graphic (Android Play Store)
  - [ ] App description and keywords
  - [ ] Privacy policy URL
- [ ] Android: generate signed release AAB, upload to Play Console (internal track)
- [ ] iOS: archive with correct provisioning profiles, upload to App Store Connect (TestFlight)
- [ ] Verify analytics events firing correctly
- [ ] Run final security audit on Firestore rules

## 7.4 Deployment & Post-Launch

### Staged Rollout
- [ ] Internal stage: dev team only, both platforms, 3 days
- [ ] Beta stage: Android Internal Track / iOS TestFlight, 5–10 testers, 1 week
- [ ] Production: Google Play public + App Store public

### Post-Launch Monitoring (first 72 hours)
- [ ] Monitor Crashlytics: target zero unhandled crashes
- [ ] Monitor Performance: startup time + network latency within targets
- [ ] Monitor Firestore usage: verify read/write counts match estimates
- [ ] Collect user feedback

### Hotfix Pipeline
- [ ] Document hotfix process: branch from `main`, fix, test, release
- [ ] Android: staged rollout plan (10% → 50% → 100%)
- [ ] iOS: expedited review process for critical issues

### Operational Runbook
- [ ] Document: how to add new tailors and user accounts
- [ ] Document: password reset process
- [ ] Document: data export / backup procedure
- [ ] Document: handling sync conflicts manually
- [ ] Document: Firebase scaling plan if user base grows

---

## Deliverables Checklist

- [ ] Full test suite: unit, integration, UI, and sync engine tests
- [ ] QA sign-off and UAT feedback incorporated
- [ ] App published on Google Play Store and Apple App Store
- [ ] Crashlytics and performance monitoring active
- [ ] Operational runbook and post-launch monitoring plan
