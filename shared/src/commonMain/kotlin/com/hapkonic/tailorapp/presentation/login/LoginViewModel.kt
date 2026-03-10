package com.hapkonic.tailorapp.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hapkonic.tailorapp.data.local.BiometricAuthService
import com.hapkonic.tailorapp.data.local.BiometricResult
import com.hapkonic.tailorapp.domain.repository.AuthRepository
import com.hapkonic.tailorapp.domain.usecase.SignInUseCase
import com.hapkonic.tailorapp.domain.usecase.SignOutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val signInUseCase: SignInUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val biometricAuthService: BiometricAuthService,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(biometricAvailable = biometricAuthService.isAvailable) }
    }

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, errorMessage = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, errorMessage = null) }
    }

    fun signIn() {
        val state = _uiState.value
        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Email and password are required.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            signInUseCase(state.email, state.password)
                .onSuccess { _uiState.update { it.copy(isLoading = false, isSignedIn = true) } }
                .onFailure { err ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = err.message ?: "Sign-in failed.")
                    }
                }
        }
    }

    fun biometricSignIn() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = biometricAuthService.authenticate()) {
                is BiometricResult.Success -> {
                    val user = authRepository.signInWithBiometric()
                    if (user != null) {
                        _uiState.update { it.copy(isLoading = false, isSignedIn = true) }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "No saved account. Sign in with email/password first."
                            )
                        }
                    }
                }
                is BiometricResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch { signOutUseCase() }
        _uiState.update { LoginUiState(biometricAvailable = biometricAuthService.isAvailable) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
