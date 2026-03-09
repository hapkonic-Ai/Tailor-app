package com.hapkonic.tailorapp.data.sync

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

actual class NetworkMonitor(private val context: Context) {

    private val _isOnline = MutableStateFlow(false)
    actual val isOnline: StateFlow<Boolean> get() = _isOnline

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) { _isOnline.value = true }
        override fun onLost(network: Network)      { _isOnline.value = false }
    }

    actual fun startMonitoring() {
        // Emit initial state
        val caps = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        _isOnline.value = caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(request, callback)
    }

    actual fun stopMonitoring() {
        connectivityManager.unregisterNetworkCallback(callback)
    }
}
