package com.hapkonic.tailorapp.di

import com.hapkonic.tailorapp.data.local.DatabaseDriverFactory
import com.hapkonic.tailorapp.data.sync.NetworkMonitor
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single { DatabaseDriverFactory() }
    single { NetworkMonitor() }
}
