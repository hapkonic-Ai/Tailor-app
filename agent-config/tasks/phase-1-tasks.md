# Phase 1 — Foundation & Architecture Tasks

**Duration:** Weeks 1–3
**Team:** Lead Developer + DevOps
**Status:** In Progress
**Started:** 2026-03-09
**Completed:** —

---

## 1.1 KMP Project Scaffolding

- [x] Initialize KMP project with Compose Multiplatform plugin (Android + iOS targets)
- [x] Create three-module structure: `androidApp`, `iosApp`, `shared`
- [ ] Configure `shared` module with `data`, `domain`, `presentation` packages (Clean Architecture) ← Phase 2 start
- [x] Set up `gradle/libs.versions.toml` with all dependencies (kotlin, compose, sqldelight, ktor, koin, coroutines, firebase-kotlin, serialization, datetime)
- [x] Configure Kotlin serialization plugin
- [ ] Verify project compiles and runs Hello World on Android emulator ← requires `google-services.json`
- [ ] Verify project compiles and runs Hello World on iOS simulator ← requires Xcode project setup

## 1.2 Build & CI/CD Setup

- [x] Configure Android Gradle Plugin (AGP) for Android build
- [ ] Configure CocoaPods or SPM integration for iOS ← requires Xcode project + `pod install`
- [x] Set up GitHub Actions workflow file (`.github/workflows/ci.yml`)
  - [x] Lint job: Detekt + Ktlint on every PR
  - [x] Build Shared job: compile shared module (JVM + native targets)
  - [x] Build Android job: assemble debug APK
  - [x] Build iOS job: build iOS framework on macOS runner
  - [x] Test job: run shared module unit tests
- [x] Create debug and release build variants
- [ ] Configure environment-specific Firebase config files (dev / staging / prod) ← manual: download from Firebase console

## 1.3 Firebase Project Configuration

- [ ] Create Firebase project with three environments (dev, staging, prod) ← manual: Firebase console
- [ ] Register Android app in Firebase console, download `google-services.json` ← manual
- [ ] Register iOS app in Firebase console, download `GoogleService-Info.plist` ← manual
- [ ] Enable Firestore in Firebase console ← manual
- [ ] Enable Firebase Storage in Firebase console ← manual
- [ ] Enable Firebase Authentication in Firebase console ← manual
- [x] Deploy initial Firestore security rules (`firestore.rules` created)
- [ ] Verify security rules with Firebase emulator ← after Firebase project is set up

## 1.4 Dependency Integration

- [x] Integrate SQLDelight with Android SQLite driver (`sqldelight-android-driver`)
- [x] Integrate SQLDelight with Native SQLite driver for iOS (`sqldelight-native-driver`)
- [x] Add Ktor client core + OkHttp engine (Android) + Darwin engine (iOS)
- [x] Set up Firebase KMP wrapper with expect/actual declarations for Firestore, Auth, Storage ← in libs.versions.toml, commented in build.gradle until credentials added
- [x] Integrate Koin DI — create `AppModule.kt` in `shared/src/commonMain`
- [ ] Verify all dependencies resolve without conflicts ← run `./gradlew build` after adding google-services.json
- [ ] Write smoke test to verify DI graph initializes on both platforms ← Phase 2

## 1.5 Project Conventions Documentation

- [x] Document branching strategy (`main`, `develop`, `feature/*`, `fix/*`) ← see below
- [x] Document commit message format (`feat(phase-N): ...`) ← established by commits
- [x] Document package structure conventions ← see overall-implementation.md
- [x] Update [phase-1-impl.md](../implementations/phase-1-impl.md) with all files created

---

## Branching Strategy

| Branch | Purpose |
|--------|---------|
| `main` | Production-ready code only |
| `develop` | Integration branch, CI must pass |
| `feature/phase-N-*` | Feature branches per phase/task |
| `fix/*` | Bug fixes |

## Commit Message Format

```
<type>(phase-N): <short description>

Types: feat, fix, chore, refactor, test, docs, ci
Examples:
  feat(phase-2): add SQLDelight schema for customers table
  fix(phase-3): handle token refresh on 401 response
  chore(phase-1): update libs.versions.toml with missing detekt dep
```

---

## Deliverables Checklist

- [ ] Compiling KMP project that runs Hello World on both Android and iOS ← pending Firebase credentials + iOS Xcode setup
- [x] CI/CD pipeline running lint + build on every pull request
- [ ] Firebase project configured with dev environment credentials ← manual steps pending
- [x] SQLDelight, Ktor, and DI framework integrated and verified (in build files)
- [x] Documented project conventions (branching, commit format, package structure)
