package com.hapkonic.tailorapp.data.sync

import kotlinx.coroutines.flow.StateFlow

/** Observes device network connectivity. Platform-specific implementation. */
expect class NetworkMonitor {
    val isOnline: StateFlow<Boolean>
    fun startMonitoring()
    fun stopMonitoring()
}
