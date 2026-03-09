package com.hapkonic.tailorapp.presentation.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

data class OrderListUiState(
    val orders: List<Order> = emptyList(),
    val selectedStatus: OrderStatus = OrderStatus.PENDING,
    val isLoading: Boolean = true
)

@OptIn(ExperimentalCoroutinesApi::class)
class OrderListViewModel(
    private val getOrdersByStatus: GetOrdersByStatusUseCase
) : ViewModel() {

    private val _status = MutableStateFlow(OrderStatus.PENDING)

    val uiState: StateFlow<OrderListUiState> = _status
        .flatMapLatest { status -> getOrdersByStatus(status) }
        .map { orders -> OrderListUiState(orders = orders, selectedStatus = _status.value, isLoading = false) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), OrderListUiState())

    fun filterByStatus(status: OrderStatus) { _status.update { status } }
}
