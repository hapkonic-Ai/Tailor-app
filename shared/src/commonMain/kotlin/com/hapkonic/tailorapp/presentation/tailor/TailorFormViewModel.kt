package com.hapkonic.tailorapp.presentation.tailor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hapkonic.tailorapp.domain.usecase.tailor.SaveTailorUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TailorFormUiState(
    val name: String = "",
    val phone: String = "",
    val specialization: String = "",
    val nameError: String? = null,
    val phoneError: String? = null,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false
)

class TailorFormViewModel(
    private val saveTailor: SaveTailorUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TailorFormUiState())
    val uiState: StateFlow<TailorFormUiState> = _uiState

    fun onNameChange(v: String)           = _uiState.update { it.copy(name = v, nameError = null) }
    fun onPhoneChange(v: String)          = _uiState.update { it.copy(phone = v, phoneError = null) }
    fun onSpecializationChange(v: String) = _uiState.update { it.copy(specialization = v) }

    fun save() {
        val s = _uiState.value
        var hasError = false
        if (s.name.isBlank()) {
            _uiState.update { it.copy(nameError = "Name is required") }
            hasError = true
        }
        if (s.phone.isBlank()) {
            _uiState.update { it.copy(phoneError = "Phone is required") }
            hasError = true
        }
        if (hasError) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            saveTailor(saveTailor.newTailor(s.name, s.phone, s.specialization))
            _uiState.update { it.copy(isSaving = false, isSaved = true) }
        }
    }
}
