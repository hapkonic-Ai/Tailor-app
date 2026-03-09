package com.hapkonic.tailorapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.hapkonic.tailorapp.domain.model.AppUser
import com.hapkonic.tailorapp.presentation.customer.CustomerDetailScreen
import com.hapkonic.tailorapp.presentation.customer.CustomerFormScreen
import com.hapkonic.tailorapp.presentation.customer.CustomerListScreen
import com.hapkonic.tailorapp.presentation.login.LoginScreen
import com.hapkonic.tailorapp.presentation.measurement.MeasurementFormScreen
import com.hapkonic.tailorapp.presentation.order.CreateOrderScreen
import com.hapkonic.tailorapp.presentation.order.OrderDetailScreen
import com.hapkonic.tailorapp.presentation.order.OrderListScreen
import com.hapkonic.tailorapp.presentation.tailor.TailorListScreen
import com.hapkonic.tailorapp.presentation.tailor.TailorOrdersScreen

/**
 * Root navigation graph. Renders the correct screen based on auth state and [AppNavigator].
 *
 * @param currentUser null = not signed in → always shows Login
 */
@Composable
fun AppNavGraph(currentUser: AppUser?) {
    val initialScreen = if (currentUser == null) Screen.Login else Screen.OrderList
    val navigator = rememberNavigator(initialScreen)

    CompositionLocalProvider(LocalNavigator provides navigator) {
        when (val screen = navigator.current) {
            is Screen.Login         -> LoginScreen(onSignedIn = { navigator.replace(Screen.OrderList) })
            is Screen.CustomerList  -> CustomerListScreen()
            is Screen.CustomerDetail -> CustomerDetailScreen(screen.customerId)
            is Screen.CustomerForm  -> CustomerFormScreen(screen.customerId)
            is Screen.OrderList     -> OrderListScreen(currentUser = currentUser)
            is Screen.OrderDetail   -> OrderDetailScreen(screen.orderId, currentUser = currentUser)
            is Screen.CreateOrder   -> CreateOrderScreen(preselectedCustomerId = screen.customerId)
            is Screen.MeasurementForm -> MeasurementFormScreen(screen.customerId, screen.measurementId)
            is Screen.TailorList    -> TailorListScreen()
            is Screen.TailorOrders  -> TailorOrdersScreen(screen.tailorId)
        }
    }
}
