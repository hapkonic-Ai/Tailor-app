# Phase 3 — Authentication & Role-Based Access Tasks

**Duration:** Weeks 8–9
**Team:** Full-stack Developer
**Status:** Complete
**Started:** 2026-03-09
**Completed:** 2026-03-09

---

## 3.1 Firebase Auth Integration

- [x] Add Firebase Authentication SDK dependency (already in libs.versions.toml via firebase-auth)
- [x] Create `AppUser` data class (`uid`, `email`, `role`, `displayName`, `photoUrl`)
- [x] Create `AuthRepository` interface with:
  - [x] `currentUser: Flow<AppUser?>`
  - [x] `suspend fun signIn(email, password): AppUser`
  - [x] `suspend fun signOut()`
  - [x] `suspend fun getCurrentUser(): AppUser?`
- [x] Implement `AuthRepositoryImpl.kt` using Firebase Auth SDK (via FirebaseAuthService)
- [ ] Implement auth token storage (Android Keystore / iOS Keychain) — Phase 6 hardening
- [ ] Implement auto token refresh — handled by Firebase SDK automatically
- [x] Register `AuthRepository` in `AppModule.kt`

## 3.2 Role Management

- [x] `UserRole` enum already exists from Phase 2 (`ADMIN`, `TAILOR`)
- [x] Create Firebase Cloud Function `setUserRole` (Node.js in `functions/`):
  - [x] Validate caller is Admin (checks JWT `role` claim)
  - [x] Use `admin.auth().setCustomUserClaims()` to assign role
- [ ] Deploy Cloud Function to Firebase — manual step (run `firebase deploy --only functions`)
- [x] On sign-in: decode JWT token, extract `role` custom claim, map to `UserRole`
- [x] Create `RoleGuard.kt`:
  - [x] `requireAdmin(user)` — throws UnauthorizedException
  - [x] `requireTailor(user)` — throws UnauthorizedException if null
  - [x] `canModifyOrder(user, order): Boolean`
  - [x] `canViewAnalytics(user): Boolean`

## 3.3 Firestore Security Rules

- [x] Full security rules already written in Phase 1 (`firestore.rules`)
- [ ] Deploy rules — manual step (`firebase deploy --only firestore:rules`)
- [ ] Test rules with Firebase emulator using role-specific test accounts
- [ ] Verify tailor cannot write to admin-only collections
- [ ] Verify tailor can only update `status` field on own assigned orders

## 3.4 Login UI

- [x] Create `LoginUiState.kt` (`email`, `password`, `isLoading`, `errorMessage`, `isSignedIn`)
- [x] Create `LoginViewModel.kt`:
  - [x] `onEmailChange()`, `onPasswordChange()`
  - [x] `signIn()` — calls `SignInUseCase`, updates state
  - [x] `signOut()`, `clearError()`
  - [x] Blank field validation
- [x] Create `SignInUseCase.kt`
- [x] Create `LoginScreen.kt` (Compose Multiplatform + Material 3):
  - [x] Email `OutlinedTextField` with keyboard type Email
  - [x] Password `OutlinedTextField` with `PasswordVisualTransformation`
  - [x] Sign In `Button` with loading `CircularProgressIndicator`
  - [x] Error shown via `SnackbarHost`
- [x] Handle navigation post-login: `onSignedIn` callback → recomposition to DashboardPlaceholder

## 3.5 Auth State Management

- [x] Implement `GetCurrentUserUseCase.kt` — wraps `authRepository.currentUser` Flow
- [x] Implement `SignOutUseCase.kt`
- [x] On app launch: `App.kt` collects `authRepository.currentUser` — shows Login or Dashboard
- [x] On sign-out: `LoginViewModel.signOut()` → clears state → Login shown

## 3.6 Tests

- [ ] Unit tests: `LoginViewModel` — Phase 7
- [ ] Unit tests: `RoleGuard` — Phase 7
- [ ] Manual test: Admin login → full access
- [ ] Manual test: Tailor login → restricted access

---

## Deliverables Checklist

- [x] Firebase Auth integration with email/password sign-in on both platforms
- [x] Role-based access control with Admin and Tailor roles
- [x] Firestore security rules written (deploy is manual)
- [x] Login screen with validation and error handling
- [x] Auth state management driving app-level routing
- [x] Cloud Function `setUserRole` for admin-assigned roles
