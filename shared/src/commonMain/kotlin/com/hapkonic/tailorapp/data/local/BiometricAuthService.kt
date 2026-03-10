package com.hapkonic.tailorapp.data.local

sealed class BiometricResult {
    object Success : BiometricResult()
    data class Error(val message: String) : BiometricResult()
}

expect class BiometricAuthService() {
    val isAvailable: Boolean
    suspend fun authenticate(): BiometricResult
}
