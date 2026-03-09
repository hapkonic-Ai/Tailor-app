# Phase 3 — Authentication & RBAC — Implementation Log

**Date Started:** 2026-03-09
**Last Updated:** 2026-03-09
**Status:** Complete

---

## Files Created / Modified

| File Path | Action | Notes |
|-----------|--------|-------|
| `gradle/libs.versions.toml` | Updated | Added lifecycle-viewmodel 2.8.7 version + library entries |
| `shared/build.gradle.kts` | Updated | Added lifecycle-viewmodel to commonMain dependencies |
| `shared/.../domain/model/AppUser.kt` | Created | @Serializable, uid/email/displayName/role/photoUrl |
| `shared/.../domain/repository/AuthRepository.kt` | Created | Interface: currentUser Flow, signIn, signOut, getCurrentUser |
| `shared/.../domain/usecase/SignInUseCase.kt` | Created | Returns Result<AppUser> via runCatching |
| `shared/.../domain/usecase/SignOutUseCase.kt` | Created | Delegates to AuthRepository |
| `shared/.../domain/usecase/GetCurrentUserUseCase.kt` | Created | Returns Flow<AppUser?> |
| `shared/.../domain/auth/RoleGuard.kt` | Created | requireAdmin, requireTailor, canModifyOrder, canViewAnalytics + UnauthorizedException |
| `shared/.../data/remote/FirebaseAuthService.kt` | Created | GitLive auth, extracts role from JWT custom claim |
| `shared/.../data/repository/AuthRepositoryImpl.kt` | Created | Delegates to FirebaseAuthService |
| `shared/.../presentation/login/LoginUiState.kt` | Created | email, password, isLoading, errorMessage, isSignedIn |
| `shared/.../presentation/login/LoginViewModel.kt` | Created | ViewModel with sign-in, sign-out, clearError, blank-field validation |
| `shared/.../presentation/login/LoginScreen.kt` | Created | Compose MP + Material 3; OutlinedTextField, PasswordVisualTransformation, Snackbar |
| `shared/.../App.kt` | Updated | Auth-state routing: collectAsState on currentUser, shows LoginScreen or DashboardPlaceholder |
| `shared/.../di/AppModule.kt` | Updated | Added FirebaseAuthService, AuthRepository, RoleGuard, 3 auth use cases, LoginViewModel |
| `functions/index.js` | Created | Cloud Function setUserRole — admin-only, sets JWT custom claim |
| `functions/package.json` | Created | Node 20, firebase-admin 12, firebase-functions 5 |

## Commits

| Commit Hash | Message | Date |
|-------------|---------|------|
| TBD | feat(phase-3): Firebase Auth, RBAC, LoginScreen | 2026-03-09 |

## Deviations from Plan

- `AuthRepository.currentUser` uses `Flow<AppUser?>` instead of `StateFlow<AppUser?>` — avoids needing an initial value at the interface level; callers use `collectAsState(initial = null)`
- Token storage (Android Keystore / iOS Keychain) deferred to Phase 6 — Firebase SDK handles refresh internally
- Emulator testing for Firestore rules deferred — rules were written in Phase 1; Phase 7 testing will cover this
- `LoginViewModel.signIn()` validates blank fields only (no regex email check) — minimal viable guard; can be enhanced in Phase 6

## Known Issues / TODOs for Phase 4

- `DashboardPlaceholder` in `App.kt` will be replaced with real navigation graph
- Cloud Function must be deployed manually: `cd functions && npm install && firebase deploy --only functions`
- Firestore rules must be deployed: `firebase deploy --only firestore:rules`
- First admin user must be assigned via Firebase Console (direct Firestore write to set role claim), then subsequent users can be assigned via the Cloud Function
