# Production Launch Runbook

**App:** Tailor Shop Management App (KMP)
**Created:** 2026-03-09

---

## Pre-Launch Checklist

### Firebase Setup
- [ ] Switch Firebase project to production (separate from dev project)
- [ ] Enable Firestore daily automated backups
- [ ] Set Firestore security rules — restrict read/write to authenticated UIDs only
- [ ] Configure billing alerts: ₹100 warning + ₹200 critical in Firebase console
- [ ] Enable Firebase Crashlytics for Android and iOS
- [ ] Enable Firebase Performance Monitoring

### Android Release
- [ ] Update `versionCode` and `versionName` in `android/build.gradle.kts`
- [ ] Generate signed release AAB: `./gradlew :android:bundleRelease`
- [ ] Upload to Google Play Console → Internal Testing track
- [ ] Complete Play Store listing: icon, screenshots, description, privacy policy URL
- [ ] Verify Crashlytics is reporting (force-crash test build)

### iOS Release
- [ ] Update `CFBundleShortVersionString` and `CFBundleVersion` in `Info.plist`
- [ ] Archive with Xcode using correct Distribution provisioning profile
- [ ] Upload to App Store Connect → TestFlight (internal group)
- [ ] Complete App Store listing: icon, screenshots, description, privacy policy URL
- [ ] Verify Crashlytics is reporting

---

## User Account Management

### Adding a New Tailor Account
1. Open `LocalAuthService.kt`
2. Add entry to the `credentials` map:
   ```kotlin
   "tailor2@shop.com" to Pair("password123", UserRole.TAILOR)
   ```
3. Build and redeploy.

> **Note:** Phase 6 TODO — replace in-memory credential map with SQLDelight-backed user table + bcrypt hashing before production.

### Password Reset
Currently manual — update the credential in `LocalAuthService.kt` and redeploy.
Future: add a `resetPassword(email, newPassword)` method to `LocalAuthService`.

---

## Data Export / Backup

### CSV Export (from app)
- Admin opens Revenue screen → taps Export
- `buildOrderCsv()` generates the CSV string
- `saveCsvFile("orders_YYYY-MM-DD.csv", content)` writes to device storage
- Android: `getExternalFilesDir("exports")/orders_*.csv`
- iOS: `NSDocumentDirectory/orders_*.csv`

### Manual Firestore Backup
1. Go to Firebase Console → Firestore → Import/Export
2. Export to Cloud Storage bucket (configure automated daily exports)
3. Retain last 30 days of backups

---

## Handling Sync Conflicts

Conflicts are resolved automatically by `ConflictResolver` using last-write-wins:
- The entity with the highest `updatedAt` timestamp wins.
- On a tie, the remote (Firestore) version wins.
- Permanently failed sync items are logged with: `[SyncManager] PERMANENT FAILURE: ...`

**Manual resolution:** If an item is permanently stuck in the sync queue:
1. Check the SQLDelight `sync_queue` table for items with `retryCount >= 5`
2. Re-save the entity from the app to reset the queue entry
3. Or delete the `sync_queue` row directly via a debug build

---

## Firebase Scaling Plan

| Threshold | Action |
|-----------|--------|
| > 10,000 customers | Enable Firestore pagination (already coded — increase `limit` from 20) |
| > 30,000 orders | Add composite Firestore index on `(status, updatedAt)` |
| > ₹200/month cost | Audit read patterns; increase local cache TTL; reduce sync frequency |
| > 100 concurrent users | Review Firestore connection limits; consider Firestore emulator load test |

---

## Staged Rollout Plan

| Stage | Target | Duration |
|-------|--------|----------|
| Internal | Dev team (2–3 people) | 3 days |
| Beta | Android Internal Track / iOS TestFlight (5–10 testers) | 1 week |
| Production | Public release (10% → 50% → 100% over 2 weeks) | 2 weeks |

---

## Post-Launch Monitoring (First 72 Hours)

- **Crashlytics:** Zero tolerance for unhandled crashes. Investigate any new crash immediately.
- **Performance:** App startup < 2s; Firestore reads < 500ms on 4G.
- **Firestore usage:** Verify daily reads stay within ₹0–10/day estimate.
- **User feedback:** Monitor Play Store / App Store reviews and in-app feedback.

---

## Hotfix Process

1. Branch from `main`: `git checkout -b hotfix/issue-description`
2. Fix the issue, write a regression test
3. Build and test on both platforms
4. Merge to `main`, tag as `vX.Y.Z-hotfix`
5. Android: staged rollout (10% first, monitor 24h, then 100%)
6. iOS: submit for expedited review if crash-level severity
