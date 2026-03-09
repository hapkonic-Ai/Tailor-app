package com.hapkonic.tailorapp.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hapkonic.tailorapp.domain.model.Customer
import com.hapkonic.tailorapp.domain.model.Order
import com.hapkonic.tailorapp.domain.usecase.customer.SearchCustomersUseCase
import com.hapkonic.tailorapp.domain.usecase.order.GetOrdersByStatusUseCase
import com.hapkonic.tailorapp.domain.model.OrderStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

data class SearchUiState(
    val query: String = "",
    val customers: List<Customer> = emptyList(),
    val orders: List<Order> = emptyList(),
    val isSearching: Boolean = false
)

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class SearchViewModel(
    private val searchCustomers: SearchCustomersUseCase,
    private val getOrdersByStatus: GetOrdersByStatusUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("")

    val uiState: StateFlow<SearchUiState> = _query
        .debounce(300)
        .flatMapLatest { q ->
            if (q.isBlank()) {
                flowOf(SearchUiState(query = q))
            } else {
                // Search customers + all active orders and filter by ID prefix client-side
                combine(
                    searchCustomers(q),
                    getOrdersByStatus(OrderStatus.PENDING, limit = 100),
                    getOrdersByStatus(OrderStatus.IN_PROGRESS, limit = 100),
                    getOrdersByStatus(OrderStatus.READY, limit = 100)
                ) { customers, pending, inProg, ready ->
                    val allOrders = (pending + inProg + ready)
                        .filter { it.id.startsWith(q, ignoreCase = true) }
                    SearchUiState(
                        query     = q,
                        customers = customers,
                        orders    = allOrders,
                        isSearching = false
                    )
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SearchUiState())

    fun onQueryChange(q: String) { _query.update { q } }
}
