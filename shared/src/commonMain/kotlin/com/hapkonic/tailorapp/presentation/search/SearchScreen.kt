package com.hapkonic.tailorapp.presentation.search

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hapkonic.tailorapp.navigation.LocalNavigator
import com.hapkonic.tailorapp.navigation.Screen
import com.hapkonic.tailorapp.ui.components.EmptyState
import com.hapkonic.tailorapp.ui.components.OrderStatusChip
import com.hapkonic.tailorapp.ui.components.SearchBar
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewModel: SearchViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val navigator = LocalNavigator.current

    Scaffold(
        topBar = { TopAppBar(title = { Text("Search") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            SearchBar(
                query = uiState.query,
                onQueryChange = viewModel::onQueryChange,
                placeholder = "Search customers or order ID…",
                modifier = Modifier.padding(vertical = 8.dp)
            )

            when {
                uiState.query.isBlank() ->
                    EmptyState("Type to search customers or order IDs")

                uiState.customers.isEmpty() && uiState.orders.isEmpty() ->
                    EmptyState("No results for \"${uiState.query}\"")

                else -> LazyColumn(
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (uiState.customers.isNotEmpty()) {
                        item {
                            Text(
                                "Customers (${uiState.customers.size})",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                        items(uiState.customers) { customer ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { navigator.navigate(Screen.CustomerDetail(customer.id)) }
                                    .padding(vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(customer.name, style = MaterialTheme.typography.bodyLarge)
                                    Text(customer.phone, style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            HorizontalDivider()
                        }
                    }

                    if (uiState.orders.isNotEmpty()) {
                        item {
                            Text(
                                "Orders (${uiState.orders.size})",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
                            )
                        }
                        items(uiState.orders) { order ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { navigator.navigate(Screen.OrderDetail(order.id)) }
                                    .padding(vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Order #${order.id.take(8)}",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                OrderStatusChip(order.status)
                            }
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}
