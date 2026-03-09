package com.hapkonic.tailorapp.di

import org.koin.core.module.Module

/** Platform-specific DI bindings (DatabaseDriverFactory, NetworkMonitor need platform context). */
expect val platformModule: Module
