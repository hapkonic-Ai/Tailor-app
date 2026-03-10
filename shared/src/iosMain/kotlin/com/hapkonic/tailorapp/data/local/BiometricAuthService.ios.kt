package com.hapkonic.tailorapp.data.local

import kotlinx.coroutines.suspendCancellableCoroutine
import platform.LocalAuthentication.LAContext
import platform.LocalAuthentication.LAPolicyDeviceOwnerAuthenticationWithBiometrics
import kotlin.coroutines.resume

actual class BiometricAuthService actual constructor() {

    actual val isAvailable: Boolean
        get() = LAContext().canEvaluatePolicy(
            LAPolicyDeviceOwnerAuthenticationWithBiometrics,
            error = null
        )

    actual suspend fun authenticate(): BiometricResult = suspendCancellableCoroutine { cont ->
        val context = LAContext()
        context.evaluatePolicy(
            LAPolicyDeviceOwnerAuthenticationWithBiometrics,
            localizedReason = "Sign in to Tailor App"
        ) { success, error ->
            if (cont.isActive) {
                if (success) cont.resume(BiometricResult.Success)
                else cont.resume(BiometricResult.Error(error?.localizedDescription ?: "Authentication failed"))
            }
        }
    }
}
