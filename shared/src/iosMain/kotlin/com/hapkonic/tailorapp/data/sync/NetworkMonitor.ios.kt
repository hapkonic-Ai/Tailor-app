package com.hapkonic.tailorapp.data.sync

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import platform.Network.nw_path_monitor_cancel
import platform.Network.nw_path_monitor_create
import platform.Network.nw_path_monitor_set_queue
import platform.Network.nw_path_monitor_set_update_handler
import platform.Network.nw_path_monitor_start
import platform.Network.nw_path_status_satisfied
import platform.Network.nw_path_get_status
import platform.darwin.DISPATCH_QUEUE_SERIAL
import platform.darwin.dispatch_queue_create

actual class NetworkMonitor {

    private val _isOnline = MutableStateFlow(true)
    actual val isOnline: StateFlow<Boolean> get() = _isOnline

    private val monitor = nw_path_monitor_create()
    private val queue   = dispatch_queue_create("com.hapkonic.tailorapp.network", DISPATCH_QUEUE_SERIAL)

    actual fun startMonitoring() {
        nw_path_monitor_set_update_handler(monitor) { path ->
            _isOnline.value = nw_path_get_status(path) == nw_path_status_satisfied
        }
        nw_path_monitor_set_queue(monitor, queue)
        nw_path_monitor_start(monitor)
    }

    actual fun stopMonitoring() {
        nw_path_monitor_cancel(monitor)
    }
}
