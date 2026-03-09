package com.hapkonic.tailorapp.presentation.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hapkonic.tailorapp.data.sync.SyncManager
import com.hapkonic.tailorapp.domain.model.Customer
import com.hapkonic.tailorapp.domain.usecase.customer.GetCustomersUseCase
import com.hapkonic.tailorapp.domain.usecase.customer.SearchCustomersUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CustomerListUiState(
    val customers: List<Customer> = emptyList(),
    val query: String = "",
    val isLoading: Boolean = true,
    val isSyncing: Boolean = false,
    val error: String? = null
)

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class CustomerListViewModel(
    getCustomers: GetCustomersUseCase,
    searchCustomers: SearchCustomersUseCase,
    private val syncManager: SyncManager
) : ViewModel() {

    private val _query = MutableStateFlow("")
    private val _isSyncing = MutableStateFlow(false)

    val uiState: StateFlow<CustomerListUiState> = _query
        .debounce(300)
        .flatMapLatest { q ->
            if (q.isBlank()) getCustomers() else searchCustomers(q)
        }
        .map { list ->
            CustomerListUiState(customers = list, isLoading = false, query = _query.value)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CustomerListUiState())

    val isSyncing: StateFlow<Boolean> = _isSyncing

    fun onQueryChange(q: String) { _query.update { q } }

    fun refresh() {
        viewModelScope.launch {
            _isSyncing.update { true }
            runCatching { syncManager.processQueue() }
            _isSyncing.update { false }
        }
    }
}
