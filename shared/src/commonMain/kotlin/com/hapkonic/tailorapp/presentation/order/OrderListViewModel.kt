package com.hapkonic.tailorapp.presentation.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hapkonic.tailorapp.data.sync.SyncManager
import com.hapkonic.tailorapp.domain.model.Order
import com.hapkonic.tailorapp.domain.model.OrderStatus
import com.hapkonic.tailorapp.domain.usecase.order.GetOrdersByStatusUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OrderListUiState(
    val orders: List<Order> = emptyList(),
    val selectedStatus: OrderStatus = OrderStatus.PENDING,
    val isLoading: Boolean = true,
    val isSyncing: Boolean = false
)

@OptIn(ExperimentalCoroutinesApi::class)
class OrderListViewModel(
    private val getOrdersByStatus: GetOrdersByStatusUseCase,
    private val syncManager: SyncManager
) : ViewModel() {

    private val _status = MutableStateFlow(OrderStatus.PENDING)
    private val _isSyncing = MutableStateFlow(false)

    val uiState: StateFlow<OrderListUiState> = _status
        .flatMapLatest { status -> getOrdersByStatus(status) }
        .map { orders -> OrderListUiState(orders = orders, selectedStatus = _status.value, isLoading = false) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), OrderListUiState())

    val isSyncing: StateFlow<Boolean> = _isSyncing

    fun filterByStatus(status: OrderStatus) { _status.update { status } }

    fun refresh() {
        viewModelScope.launch {
            _isSyncing.update { true }
            runCatching { syncManager.processQueue() }
            _isSyncing.update { false }
        }
    }
}
