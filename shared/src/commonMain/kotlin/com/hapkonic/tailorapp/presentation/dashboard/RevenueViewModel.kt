package com.hapkonic.tailorapp.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hapkonic.tailorapp.data.sync.currentTimeMillis
import com.hapkonic.tailorapp.domain.usecase.dashboard.GetRevenueReportUseCase
import com.hapkonic.tailorapp.domain.usecase.dashboard.RevenueReport
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

data class RevenueUiState(
    val report: RevenueReport = RevenueReport(),
    val startDate: Long = 0L,
    val endDate: Long = 0L,
    val isLoading: Boolean = true
)

@OptIn(ExperimentalCoroutinesApi::class)
class RevenueViewModel(
    private val getRevenueReport: GetRevenueReportUseCase
) : ViewModel() {

    // Default: last 30 days
    private val _range = MutableStateFlow(defaultRange())

    val uiState: StateFlow<RevenueUiState> = _range
        .flatMapLatest { (start, end) ->
            getRevenueReport(start, end).map { report ->
                RevenueUiState(report = report, startDate = start, endDate = end, isLoading = false)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), RevenueUiState())

    fun setRange(startDate: Long, endDate: Long) = _range.update { Pair(startDate, endDate) }

    fun setThisMonth() {
        val now = currentTimeMillis()
        val start = now - 30L * 24 * 60 * 60 * 1000
        _range.update { Pair(start, now) }
    }

    fun setThisWeek() {
        val now = currentTimeMillis()
        val start = now - 7L * 24 * 60 * 60 * 1000
        _range.update { Pair(start, now) }
    }

    private fun defaultRange(): Pair<Long, Long> {
        val now = currentTimeMillis()
        return Pair(now - 30L * 24 * 60 * 60 * 1000, now)
    }
}
