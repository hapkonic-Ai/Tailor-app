package com.hapkonic.tailorapp.presentation.tailor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hapkonic.tailorapp.data.sync.SyncManager
import com.hapkonic.tailorapp.domain.model.Tailor
import com.hapkonic.tailorapp.domain.usecase.tailor.GetTailorsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TailorListUiState(
    val tailors: List<Tailor> = emptyList(),
    val isLoading: Boolean = true,
    val isSyncing: Boolean = false
)

class TailorListViewModel(
    getTailors: GetTailorsUseCase,
    private val syncManager: SyncManager
) : ViewModel() {

    private val _isSyncing = MutableStateFlow(false)

    val uiState: StateFlow<TailorListUiState> = getTailors()
        .map { TailorListUiState(tailors = it, isLoading = false) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TailorListUiState())

    val isSyncing: StateFlow<Boolean> = _isSyncing

    fun refresh() {
        viewModelScope.launch {
            _isSyncing.update { true }
            runCatching { syncManager.processQueue() }
            _isSyncing.update { false }
        }
    }
}
