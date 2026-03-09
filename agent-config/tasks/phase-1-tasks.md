# Phase 1 — Foundation & Architecture Tasks

**Duration:** Weeks 1–3
**Team:** Lead Developer + DevOps
**Status:** Not Started
**Started:** —
**Completed:** —

---

## 1.1 KMP Project Scaffolding

- [ ] Initialize KMP project with Compose Multiplatform plugin (Android + iOS targets)
- [ ] Create three-module structure: `androidApp`, `iosApp`, `shared`
- [ ] Configure `shared` module with `data`, `domain`, `presentation` packages (Clean Architecture)
- [ ] Set up `gradle/libs.versions.toml` with all dependencies (kotlin, compose, sqldelight, ktor, koin, coroutines, firebase-kotlin, serialization, datetime)
- [ ] Configure Kotlin serialization plugin
- [ ] Verify project compiles and runs Hello World on Android emulator
- [ ] Verify project compiles and runs Hello World on iOS simulator

## 1.2 Build & CI/CD Setup

- [ ] Configure Android Gradle Plugin (AGP) for Android build
- [ ] Configure CocoaPods or SPM integration for iOS
- [ ] Set up GitHub Actions workflow file (`.github/workflows/ci.yml`)
  - [ ] Lint job: Detekt + Ktlint on every PR
  - [ ] Build Shared job: compile shared module (JVM + native targets)
  - [ ] Build Android job: assemble debug APK
  - [ ] Build iOS job: build iOS framework on macOS runner
  - [ ] Test job: run shared module unit tests
- [ ] Create debug and release build variants
- [ ] Configure environment-specific Firebase config files (dev / staging / prod)

## 1.3 Firebase Project Configuration

- [ ] Create Firebase project with three environments (dev, staging, prod)
- [ ] Register Android app in Firebase console, download `google-services.json`
- [ ] Register iOS app in Firebase console, download `GoogleService-Info.plist`
- [ ] Enable Firestore in Firebase console
- [ ] Enable Firebase Storage in Firebase console
- [ ] Enable Firebase Authentication in Firebase console
- [ ] Deploy initial Firestore security rules (deny-all with per-collection overrides)
- [ ] Verify security rules with Firebase emulator

## 1.4 Dependency Integration

- [ ] Integrate SQLDelight with Android SQLite driver (`sqldelight-android-driver`)
- [ ] Integrate SQLDelight with Native SQLite driver for iOS (`sqldelight-native-driver`)
- [ ] Add Ktor client core + OkHttp engine (Android) + Darwin engine (iOS)
- [ ] Set up Firebase KMP wrapper with expect/actual declarations for Firestore, Auth, Storage
- [ ] Integrate Koin DI — create `AppModule.kt` in `shared/src/commonMain`
- [ ] Verify all dependencies resolve without conflicts
- [ ] Write smoke test to verify DI graph initializes on both platforms

## 1.5 Project Conventions Documentation

- [ ] Document branching strategy (e.g., `main`, `develop`, `feature/*`, `fix/*`)
- [ ] Document commit message format (e.g., `feat(phase-1): ...`)
- [ ] Document package structure conventions
- [ ] Update [phase-1-impl.md](../implementations/phase-1-impl.md) with all files created

---

## Deliverables Checklist

- [ ] Compiling KMP project that runs Hello World on both Android and iOS
- [ ] CI/CD pipeline running lint + build on every pull request
- [ ] Firebase project configured with dev environment credentials
- [ ] SQLDelight, Ktor, and DI framework integrated and verified
- [ ] Documented project conventions (branching, commit format, package structure)
