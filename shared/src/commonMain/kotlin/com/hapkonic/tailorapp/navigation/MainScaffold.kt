package com.hapkonic.tailorapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.hapkonic.tailorapp.domain.model.AppUser
import com.hapkonic.tailorapp.domain.model.UserRole
import com.hapkonic.tailorapp.presentation.customer.CustomerListScreen
import com.hapkonic.tailorapp.presentation.customer.CustomerDetailScreen
import com.hapkonic.tailorapp.presentation.customer.CustomerFormScreen
import com.hapkonic.tailorapp.presentation.dashboard.DashboardScreen
import com.hapkonic.tailorapp.presentation.measurement.MeasurementFormScreen
import com.hapkonic.tailorapp.presentation.order.CreateOrderScreen
import com.hapkonic.tailorapp.presentation.order.OrderDetailScreen
import com.hapkonic.tailorapp.presentation.order.OrderListScreen
import com.hapkonic.tailorapp.presentation.dashboard.RevenueScreen
import com.hapkonic.tailorapp.presentation.search.SearchScreen
import com.hapkonic.tailorapp.presentation.tailor.TailorListScreen
import com.hapkonic.tailorapp.presentation.tailor.TailorOrdersScreen

private data class NavItem(val label: String, val icon: ImageVector, val root: Screen)

@Composable
fun MainScaffold(currentUser: AppUser?, navigator: AppNavigator) {
    val isAdmin = currentUser?.role == UserRole.ADMIN

    val navItems = buildList {
        add(NavItem("Dashboard", Icons.Default.Dashboard, Screen.Dashboard))
        if (isAdmin) add(NavItem("Customers", Icons.Default.Group, Screen.CustomerList))
        add(NavItem("Orders", Icons.Default.List, Screen.OrderList))
        if (isAdmin) add(NavItem("Tailors", Icons.Default.Person, Screen.TailorList))
        add(NavItem("Search", Icons.Default.Search, Screen.Search))
    }

    // Determine currently selected tab (match by root screen type)
    val currentRoot = navigator.current
    val selectedIndex = navItems.indexOfFirst { it.root::class == currentRoot::class }
        .coerceAtLeast(0)

    Scaffold(
        bottomBar = {
            NavigationBar {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick  = { if (selectedIndex != index) navigator.replace(item.root) },
                        icon     = { Icon(item.icon, contentDescription = item.label) },
                        label    = { Text(item.label) }
                    )
                }
            }
        }
    ) { _ ->
        when (val screen = navigator.current) {
            is Screen.Dashboard      -> DashboardScreen()
            is Screen.CustomerList   -> CustomerListScreen(currentUser = currentUser)
            is Screen.CustomerDetail -> CustomerDetailScreen(screen.customerId)
            is Screen.CustomerForm   -> CustomerFormScreen(screen.customerId)
            is Screen.OrderList      -> OrderListScreen(currentUser = currentUser)
            is Screen.OrderDetail    -> OrderDetailScreen(screen.orderId, currentUser = currentUser)
            is Screen.CreateOrder    -> CreateOrderScreen(preselectedCustomerId = screen.customerId)
            is Screen.MeasurementForm -> MeasurementFormScreen(screen.customerId, screen.measurementId)
            is Screen.TailorList     -> TailorListScreen()
            is Screen.TailorOrders   -> TailorOrdersScreen(screen.tailorId, currentUser = currentUser)
            is Screen.Search         -> SearchScreen()
            is Screen.Revenue        -> RevenueScreen()
            else                     -> DashboardScreen()
        }
    }
}
