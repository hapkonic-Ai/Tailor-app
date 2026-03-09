package com.hapkonic.tailorapp.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hapkonic.tailorapp.domain.usecase.dashboard.DashboardMetrics
import com.hapkonic.tailorapp.domain.usecase.dashboard.GetDashboardMetricsUseCase
import com.hapkonic.tailorapp.data.sync.SyncManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DashboardUiState(
    val metrics: DashboardMetrics = DashboardMetrics(),
    val isLoading: Boolean = true,
    val isSyncing: Boolean = false,
    val error: String? = null
)

class DashboardViewModel(
    getDashboardMetrics: GetDashboardMetricsUseCase,
    private val syncManager: SyncManager
) : ViewModel() {

    val uiState: StateFlow<DashboardUiState> = getDashboardMetrics()
        .map { metrics -> DashboardUiState(metrics = metrics, isLoading = false) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DashboardUiState())

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing

    fun refresh() {
        viewModelScope.launch {
            _isSyncing.update { true }
            runCatching { syncManager.processQueue() }
            _isSyncing.update { false }
        }
    }
}
