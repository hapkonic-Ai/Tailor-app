package com.hapkonic.tailorapp.di

import org.koin.dsl.module

/**
 * Main Koin DI module for the shared module.
 *
 * Phase 1: Empty — populated incrementally per phase:
 *   Phase 2: Database, Repositories, SyncManager, RemoteServices
 *   Phase 3: AuthRepository, RoleGuard
 *   Phase 4: Use Cases, ViewModels
 */
val appModule = module {
    // Phase 2+: database, repositories, sync engine
    // Phase 3+: auth, role guard
    // Phase 4+: use cases, viewmodels
}
