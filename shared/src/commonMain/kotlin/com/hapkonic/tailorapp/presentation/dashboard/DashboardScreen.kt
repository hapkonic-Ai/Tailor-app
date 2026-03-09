package com.hapkonic.tailorapp.presentation.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hapkonic.tailorapp.ui.components.KpiCard
import com.hapkonic.tailorapp.ui.components.LoadingIndicator
import com.hapkonic.tailorapp.ui.theme.StatusInProgress
import com.hapkonic.tailorapp.ui.theme.StatusPending
import com.hapkonic.tailorapp.ui.theme.StatusReady
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: DashboardViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val isSyncing by viewModel.isSyncing.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Dashboard") }) }
    ) { padding ->
        if (uiState.isLoading) {
            LoadingIndicator()
            return@Scaffold
        }

        PullToRefreshBox(
            isRefreshing = isSyncing,
            onRefresh = viewModel::refresh,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ── KPI grid ──────────────────────────────────────────────────
                item {
                    Text("Overview", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        KpiCard(
                            label = "Pending",
                            value = uiState.metrics.pendingCount.toString(),
                            containerColor = StatusPending.copy(alpha = 0.15f),
                            contentColor   = StatusPending,
                            modifier = Modifier.weight(1f)
                        )
                        KpiCard(
                            label = "In Progress",
                            value = uiState.metrics.inProgressCount.toString(),
                            containerColor = StatusInProgress.copy(alpha = 0.15f),
                            contentColor   = StatusInProgress,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        KpiCard(
                            label = "Ready",
                            value = uiState.metrics.readyCount.toString(),
                            containerColor = StatusReady.copy(alpha = 0.15f),
                            contentColor   = StatusReady,
                            modifier = Modifier.weight(1f)
                        )
                        KpiCard(
                            label = "Active Total",
                            value = uiState.metrics.totalActiveOrders.toString(),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // ── Tailor workload ───────────────────────────────────────────
                item {
                    Spacer(Modifier.height(4.dp))
                    Text("Tailor Workload", style = MaterialTheme.typography.titleMedium)
                }

                if (uiState.metrics.tailorWorkload.isEmpty()) {
                    item {
                        Text(
                            "No tailors registered",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    items(uiState.metrics.tailorWorkload, key = { it.tailorName }) { workload ->
                        WorkloadRow(
                            name         = workload.tailorName,
                            activeOrders = workload.activeOrderCount
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WorkloadRow(name: String, activeOrders: Int) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(name, style = MaterialTheme.typography.bodyLarge)
            Text(
                "$activeOrders active",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
