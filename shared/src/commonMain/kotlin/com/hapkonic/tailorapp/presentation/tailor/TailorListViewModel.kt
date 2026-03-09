package com.hapkonic.tailorapp.presentation.tailor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hapkonic.tailorapp.domain.model.Tailor
import com.hapkonic.tailorapp.domain.usecase.tailor.GetTailorsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class TailorListUiState(
    val tailors: List<Tailor> = emptyList(),
    val isLoading: Boolean = true
)

class TailorListViewModel(getTailors: GetTailorsUseCase) : ViewModel() {
    val uiState: StateFlow<TailorListUiState> = getTailors()
        .map { TailorListUiState(tailors = it, isLoading = false) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TailorListUiState())
}
