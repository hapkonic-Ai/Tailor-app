package com.hapkonic.tailorapp.di

import com.hapkonic.tailorapp.data.local.DatabaseDriverFactory
import com.hapkonic.tailorapp.data.remote.FirebaseStorageService
import com.hapkonic.tailorapp.data.remote.FirestoreService
import com.hapkonic.tailorapp.data.remote.LocalAuthService
import com.hapkonic.tailorapp.data.repository.AuthRepositoryImpl
import com.hapkonic.tailorapp.data.repository.CustomerRepositoryImpl
import com.hapkonic.tailorapp.data.repository.MeasurementRepositoryImpl
import com.hapkonic.tailorapp.data.repository.OrderRepositoryImpl
import com.hapkonic.tailorapp.data.repository.TailorRepositoryImpl
import com.hapkonic.tailorapp.data.sync.ConflictResolver
import com.hapkonic.tailorapp.data.sync.SyncManager
import com.hapkonic.tailorapp.data.sync.SyncQueue
import com.hapkonic.tailorapp.db.AppDatabase
import com.hapkonic.tailorapp.domain.auth.RoleGuard
import com.hapkonic.tailorapp.domain.repository.AuthRepository
import com.hapkonic.tailorapp.domain.repository.CustomerRepository
import com.hapkonic.tailorapp.domain.repository.MeasurementRepository
import com.hapkonic.tailorapp.domain.repository.OrderRepository
import com.hapkonic.tailorapp.domain.repository.TailorRepository
import com.hapkonic.tailorapp.domain.usecase.GetCurrentUserUseCase
import com.hapkonic.tailorapp.domain.usecase.SignInUseCase
import com.hapkonic.tailorapp.domain.usecase.SignOutUseCase
import com.hapkonic.tailorapp.domain.usecase.customer.GetCustomerByIdUseCase
import com.hapkonic.tailorapp.domain.usecase.customer.GetCustomersUseCase
import com.hapkonic.tailorapp.domain.usecase.customer.SaveCustomerUseCase
import com.hapkonic.tailorapp.domain.usecase.customer.SearchCustomersUseCase
import com.hapkonic.tailorapp.domain.usecase.measurement.GetMeasurementsUseCase
import com.hapkonic.tailorapp.domain.usecase.measurement.SaveMeasurementUseCase
import com.hapkonic.tailorapp.domain.usecase.order.CreateOrderUseCase
import com.hapkonic.tailorapp.domain.usecase.order.GetOrderByIdUseCase
import com.hapkonic.tailorapp.domain.usecase.order.GetOrdersByCustomerUseCase
import com.hapkonic.tailorapp.domain.usecase.order.GetOrdersByStatusUseCase
import com.hapkonic.tailorapp.domain.usecase.order.GetOrdersByTailorUseCase
import com.hapkonic.tailorapp.domain.usecase.order.UpdateOrderStatusUseCase
import com.hapkonic.tailorapp.domain.usecase.tailor.GetTailorsUseCase
import com.hapkonic.tailorapp.domain.usecase.dashboard.GetDashboardMetricsUseCase
import com.hapkonic.tailorapp.domain.usecase.dashboard.GetRevenueReportUseCase
import com.hapkonic.tailorapp.presentation.customer.CustomerDetailViewModel
import com.hapkonic.tailorapp.presentation.customer.CustomerFormViewModel
import com.hapkonic.tailorapp.presentation.customer.CustomerListViewModel
import com.hapkonic.tailorapp.presentation.login.LoginViewModel
import com.hapkonic.tailorapp.presentation.measurement.MeasurementFormViewModel
import com.hapkonic.tailorapp.presentation.order.CreateOrderViewModel
import com.hapkonic.tailorapp.presentation.order.OrderDetailViewModel
import com.hapkonic.tailorapp.presentation.order.OrderListViewModel
import com.hapkonic.tailorapp.presentation.tailor.TailorListViewModel
import com.hapkonic.tailorapp.presentation.tailor.TailorOrdersViewModel
import com.hapkonic.tailorapp.presentation.dashboard.DashboardViewModel
import com.hapkonic.tailorapp.presentation.dashboard.RevenueViewModel
import com.hapkonic.tailorapp.presentation.search.SearchViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Main shared Koin DI module.
 * Phase 2: Database, Repositories, SyncManager, RemoteServices
 * Phase 3: AuthRepository, RoleGuard, Auth use cases, LoginViewModel
 * Phase 4: Feature use cases, all ViewModels, navigation
 */
val appModule = module {

    // ── Database ─────────────────────────────────────────────────────────────
    single { AppDatabase(get<DatabaseDriverFactory>().create()) }
    single { SyncQueue(get()) }

    // ── Remote Services ───────────────────────────────────────────────────────
    single { FirestoreService() }
    single { FirebaseStorageService() }
    single { LocalAuthService() }

    // ── Sync Engine ───────────────────────────────────────────────────────────
    single { ConflictResolver() }
    single {
        SyncManager(
            db               = get(),
            firestore        = get(),
            syncQueue        = get(),
            networkMonitor   = get(),
            conflictResolver = get(),
            scope            = CoroutineScope(Dispatchers.Default + SupervisorJob())
        )
    }

    // ── Repositories ──────────────────────────────────────────────────────────
    single<CustomerRepository>    { CustomerRepositoryImpl(get(), get()) }
    single<OrderRepository>       { OrderRepositoryImpl(get(), get()) }
    single<MeasurementRepository> { MeasurementRepositoryImpl(get(), get()) }
    single<TailorRepository>      { TailorRepositoryImpl(get(), get()) }
    single<AuthRepository>        { AuthRepositoryImpl(get()) }

    // ── Auth Domain ───────────────────────────────────────────────────────────
    single { RoleGuard() }
    single { SignInUseCase(get()) }
    single { SignOutUseCase(get()) }
    single { GetCurrentUserUseCase(get()) }

    // ── Customer Use Cases ────────────────────────────────────────────────────
    single { GetCustomersUseCase(get()) }
    single { GetCustomerByIdUseCase(get()) }
    single { SearchCustomersUseCase(get()) }
    single { SaveCustomerUseCase(get()) }

    // ── Order Use Cases ───────────────────────────────────────────────────────
    single { GetOrdersByStatusUseCase(get()) }
    single { GetOrdersByCustomerUseCase(get()) }
    single { GetOrdersByTailorUseCase(get()) }
    single { GetOrderByIdUseCase(get()) }
    single { CreateOrderUseCase(get()) }
    single { UpdateOrderStatusUseCase(get()) }

    // ── Measurement Use Cases ─────────────────────────────────────────────────
    single { GetMeasurementsUseCase(get()) }
    single { SaveMeasurementUseCase(get()) }

    // ── Tailor Use Cases ──────────────────────────────────────────────────────
    single { GetTailorsUseCase(get()) }

    // ── Dashboard / Analytics Use Cases ───────────────────────────────────────
    single { GetDashboardMetricsUseCase(get(), get()) }
    single { GetRevenueReportUseCase(get()) }

    // ── ViewModels ────────────────────────────────────────────────────────────
    viewModel { LoginViewModel(get(), get()) }
    viewModelOf(::CustomerListViewModel)
    viewModelOf(::CustomerFormViewModel)
    viewModel { (customerId: String) -> CustomerDetailViewModel(customerId, get(), get(), get()) }
    viewModelOf(::OrderListViewModel)
    viewModelOf(::CreateOrderViewModel)
    viewModel { (orderId: String) -> OrderDetailViewModel(orderId, get(), get()) }
    viewModelOf(::MeasurementFormViewModel)
    viewModelOf(::TailorListViewModel)
    viewModel { (tailorId: String) -> TailorOrdersViewModel(tailorId, get(), get()) }
    viewModelOf(::DashboardViewModel)
    viewModelOf(::RevenueViewModel)
    viewModelOf(::SearchViewModel)
}
