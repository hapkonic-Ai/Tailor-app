# Phase 3 — Authentication & Role-Based Access Tasks

**Duration:** Weeks 8–9
**Team:** Full-stack Developer
**Status:** Not Started
**Started:** —
**Completed:** —

---

## 3.1 Firebase Auth Integration

- [ ] Add Firebase Authentication SDK dependency (expect/actual for platform-specific code)
- [ ] Create `AppUser` data class (`uid`, `email`, `role`, `displayName`)
- [ ] Create `AuthRepository` interface with:
  - [ ] `currentUser: StateFlow<AppUser?>`
  - [ ] `isAuthenticated: StateFlow<Boolean>`
  - [ ] `suspend fun signIn(email, password): Result<AppUser>`
  - [ ] `suspend fun signOut()`
  - [ ] `fun observeAuthState(): Flow<AppUser?>`
- [ ] Implement `AuthRepositoryImpl.kt` using Firebase Auth SDK
- [ ] Implement auth token storage:
  - [ ] Android: Android Keystore
  - [ ] iOS: iOS Keychain
- [ ] Implement auto token refresh and graceful expiration handling
- [ ] Register `AuthRepository` in `AppModule.kt`

## 3.2 Role Management

- [ ] Define `UserRole` enum: `ADMIN`, `TAILOR` (may already exist from Phase 2)
- [ ] Create Firebase Cloud Function `setUserRole` (Node.js):
  - [ ] Validate caller is Admin
  - [ ] Use `admin.auth().setCustomUserClaims()` to assign role
- [ ] Deploy Cloud Function to Firebase
- [ ] On sign-in: decode JWT token, extract `role` custom claim, store in app state
- [ ] Create `RoleGuard.kt`:
  - [ ] `requireAdmin(): Boolean`
  - [ ] `requireTailor(): Boolean`
  - [ ] `canModifyOrder(order: Order): Boolean`

## 3.3 Firestore Security Rules

- [ ] Finalize and deploy full security rules:
  - [ ] Customers: read (any auth), write (admin only)
  - [ ] Orders: read (any auth), write (admin), status update (assigned tailor only, limited fields)
  - [ ] Measurements: read (any auth), write (admin only)
  - [ ] Tailors: read (any auth), write (admin only)
- [ ] Test rules with Firebase emulator using role-specific test accounts
- [ ] Verify tailor cannot write to admin-only collections
- [ ] Verify tailor can only update `status` field on own assigned orders

## 3.4 Login UI

- [ ] Create `LoginUiState.kt` (`email`, `password`, `emailError`, `passwordError`, `isLoading`, `error`)
- [ ] Create `LoginViewModel.kt`:
  - [ ] `onEmailChanged()`, `onPasswordChanged()`
  - [ ] `onSignIn()` — calls `SignInUseCase`, updates state
  - [ ] Email format validation, minimum password length validation
- [ ] Create `SignInUseCase.kt`
- [ ] Create `LoginScreen.kt` (Compose Multiplatform + Material 3):
  - [ ] Email `OutlinedTextField` with keyboard type Email
  - [ ] Password `OutlinedTextField` with `PasswordVisualTransformation`
  - [ ] Sign In `Button` with loading indicator
  - [ ] Error banner for wrong credentials / network error
- [ ] Handle navigation post-login: Admin → Dashboard, Tailor → Tailor Orders screen
- [ ] Handle edge cases: wrong credentials, network error, account disabled

## 3.5 Auth State Management

- [ ] Implement `GetCurrentUserUseCase.kt`
- [ ] Implement `SignOutUseCase.kt`
- [ ] On app launch: check auth state, navigate to Login or Dashboard accordingly
- [ ] On sign-out: clear local session, navigate to Login

## 3.6 Tests

- [ ] Unit tests: `LoginViewModel` — email/password validation, sign-in state transitions
- [ ] Unit tests: `RoleGuard` — role checks and order modification permission
- [ ] Manual test: Admin login → full access
- [ ] Manual test: Tailor login → restricted access, only own orders modifiable

---

## Deliverables Checklist

- [ ] Firebase Auth integration with email/password sign-in on both platforms
- [ ] Role-based access control with Admin and Tailor roles
- [ ] Firestore security rules deployed and tested
- [ ] Login screen with validation and error handling
- [ ] Auth state management with auto-refresh and secure storage
