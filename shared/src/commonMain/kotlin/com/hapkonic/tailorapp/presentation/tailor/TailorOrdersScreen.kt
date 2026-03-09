package com.hapkonic.tailorapp.presentation.tailor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hapkonic.tailorapp.domain.model.AppUser
import com.hapkonic.tailorapp.domain.model.Order
import com.hapkonic.tailorapp.domain.model.OrderStatus
import com.hapkonic.tailorapp.navigation.LocalNavigator
import com.hapkonic.tailorapp.navigation.Screen
import com.hapkonic.tailorapp.ui.components.EmptyState
import com.hapkonic.tailorapp.ui.components.LoadingIndicator
import com.hapkonic.tailorapp.ui.components.OrderStatusChip
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TailorOrdersScreen(
    tailorId: String,
    currentUser: AppUser? = null,
    viewModel: TailorOrdersViewModel = koinViewModel(parameters = { parametersOf(tailorId) })
) {
    val uiState by viewModel.uiState.collectAsState()
    val error by viewModel.error.collectAsState()
    val navigator = LocalNavigator.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(error) {
        error?.let { snackbarHostState.showSnackbar(it); viewModel.clearError() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Orders") },
                navigationIcon = {
                    IconButton(onClick = navigator::goBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        when {
            uiState.isLoading -> LoadingIndicator()
            uiState.ordersByStatus.isEmpty() -> EmptyState("No active orders")
            else -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OrderStatus.entries
                    .filter { it != OrderStatus.DELIVERED }
                    .forEach { status ->
                        val orders = uiState.ordersByStatus[status] ?: return@forEach
                        item {
                            Text(
                                text = "${status.name.replace('_', ' ')} (${orders.size})",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                        items(orders) { order ->
                            TailorOrderItem(
                                order = order,
                                currentUser = currentUser,
                                onAdvance = { currentUser?.let { viewModel.advanceStatus(order, it) } },
                                onDetail = { navigator.navigate(Screen.OrderDetail(order.id)) }
                            )
                        }
                    }
            }
        }
    }
}

@Composable
private fun TailorOrderItem(
    order: Order,
    currentUser: AppUser?,
    onAdvance: () -> Unit,
    onDetail: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onDetail)) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("₹${order.price}", style = MaterialTheme.typography.titleMedium)
                OrderStatusChip(order.status)
            }
            order.status.next()?.let { next ->
                Button(onClick = onAdvance, modifier = Modifier.fillMaxWidth()) {
                    Text("Mark as ${next.name.replace('_', ' ')}")
                }
            }
        }
    }
}
