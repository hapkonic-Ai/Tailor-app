package com.hapkonic.tailorapp.data.local

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

actual class BiometricAuthService actual constructor() {

    actual val isAvailable: Boolean
        get() {
            val activity = ActivityHolder.get() ?: return false
            return BiometricManager.from(activity)
                .canAuthenticate(BIOMETRIC_STRONG) == BIOMETRIC_SUCCESS
        }

    actual suspend fun authenticate(): BiometricResult = withContext(Dispatchers.Main) {
        val activity = ActivityHolder.get()
            ?: return@withContext BiometricResult.Error("Activity not available")

        suspendCancellableCoroutine { cont ->
            val executor = ContextCompat.getMainExecutor(activity)
            val prompt = BiometricPrompt(
                activity, executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        if (cont.isActive) cont.resume(BiometricResult.Success)
                    }
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        if (cont.isActive) cont.resume(BiometricResult.Error(errString.toString()))
                    }
                    // onAuthenticationFailed: user gets another attempt, don't cancel
                }
            )
            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Login")
                .setSubtitle("Sign in to Tailor App")
                .setNegativeButtonText("Use Password")
                .setAllowedAuthenticators(BIOMETRIC_STRONG)
                .build()
            prompt.authenticate(promptInfo)
            cont.invokeOnCancellation { prompt.cancelAuthentication() }
        }
    }
}
