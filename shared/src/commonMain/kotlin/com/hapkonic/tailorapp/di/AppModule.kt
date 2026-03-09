package com.hapkonic.tailorapp.di

import com.hapkonic.tailorapp.data.local.DatabaseDriverFactory
import com.hapkonic.tailorapp.data.remote.FirebaseStorageService
import com.hapkonic.tailorapp.data.remote.FirestoreService
import com.hapkonic.tailorapp.data.repository.CustomerRepositoryImpl
import com.hapkonic.tailorapp.data.repository.MeasurementRepositoryImpl
import com.hapkonic.tailorapp.data.repository.OrderRepositoryImpl
import com.hapkonic.tailorapp.data.repository.TailorRepositoryImpl
import com.hapkonic.tailorapp.data.sync.ConflictResolver
import com.hapkonic.tailorapp.data.sync.SyncManager
import com.hapkonic.tailorapp.data.sync.SyncQueue
import com.hapkonic.tailorapp.db.AppDatabase
import com.hapkonic.tailorapp.domain.repository.CustomerRepository
import com.hapkonic.tailorapp.domain.repository.MeasurementRepository
import com.hapkonic.tailorapp.domain.repository.OrderRepository
import com.hapkonic.tailorapp.domain.repository.TailorRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

/**
 * Main shared Koin DI module.
 * Platform-specific bindings (DatabaseDriverFactory, NetworkMonitor) live in [platformModule].
 *
 * Phase 2: Database, Repositories, SyncManager, RemoteServices
 * Phase 3: AuthRepository, RoleGuard (added then)
 * Phase 4: Use Cases, ViewModels (added then)
 */
val appModule = module {

    // ── Database ─────────────────────────────────────────────────────────────
    single { AppDatabase(get<DatabaseDriverFactory>().create()) }
    single { SyncQueue(get()) }

    // ── Remote Services ───────────────────────────────────────────────────────
    single { FirestoreService() }
    single { FirebaseStorageService() }

    // ── Sync Engine ───────────────────────────────────────────────────────────
    single { ConflictResolver() }
    single {
        SyncManager(
            db               = get(),
            firestore        = get(),
            syncQueue        = get(),
            networkMonitor   = get(),   // provided by platformModule
            conflictResolver = get(),
            scope            = CoroutineScope(Dispatchers.Default + SupervisorJob())
        )
    }

    // ── Repositories ──────────────────────────────────────────────────────────
    single<CustomerRepository>    { CustomerRepositoryImpl(get(), get()) }
    single<OrderRepository>       { OrderRepositoryImpl(get(), get()) }
    single<MeasurementRepository> { MeasurementRepositoryImpl(get(), get()) }
    single<TailorRepository>      { TailorRepositoryImpl(get(), get()) }

    // Phase 3+: AuthRepository, RoleGuard
    // Phase 4+: Use cases, ViewModels
}
