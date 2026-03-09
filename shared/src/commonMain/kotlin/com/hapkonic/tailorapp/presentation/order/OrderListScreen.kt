package com.hapkonic.tailorapp.presentation.order

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hapkonic.tailorapp.domain.model.AppUser
import com.hapkonic.tailorapp.domain.model.Order
import com.hapkonic.tailorapp.domain.model.OrderStatus
import com.hapkonic.tailorapp.domain.model.UserRole
import com.hapkonic.tailorapp.navigation.LocalNavigator
import com.hapkonic.tailorapp.navigation.Screen
import com.hapkonic.tailorapp.ui.components.EmptyState
import com.hapkonic.tailorapp.ui.components.LoadingIndicator
import com.hapkonic.tailorapp.ui.components.OrderStatusChip
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderListScreen(
    currentUser: AppUser? = null,
    viewModel: OrderListViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isSyncing by viewModel.isSyncing.collectAsState()
    val navigator = LocalNavigator.current

    Scaffold(
        topBar = { TopAppBar(title = { Text("Orders") }) },
        floatingActionButton = {
            if (currentUser?.role == UserRole.ADMIN) {
                FloatingActionButton(onClick = { navigator.navigate(Screen.CreateOrder()) }) {
                    Icon(Icons.Default.Add, contentDescription = "Create order")
                }
            }
        }
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = isSyncing,
            onRefresh = viewModel::refresh,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Status filter chips
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(OrderStatus.entries) { status ->
                        FilterChip(
                            selected = uiState.selectedStatus == status,
                            onClick = { viewModel.filterByStatus(status) },
                            label = { Text(status.name.replace('_', ' ').lowercase().replaceFirstChar { it.uppercase() }) }
                        )
                    }
                }

                when {
                    uiState.isLoading -> LoadingIndicator()
                    uiState.orders.isEmpty() -> EmptyState("No ${uiState.selectedStatus.name.lowercase()} orders")
                    else -> LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.orders, key = { it.id }) { order ->
                            OrderListItem(
                                order = order,
                                onClick = { navigator.navigate(Screen.OrderDetail(order.id)) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderListItem(order: Order, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Order #${order.id.take(8)}", style = MaterialTheme.typography.titleMedium)
                Text("₹${order.price}", style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            OrderStatusChip(order.status)
        }
    }
}
