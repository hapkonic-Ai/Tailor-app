package com.hapkonic.tailorapp.navigation

/** All navigation destinations in the app. */
sealed class Screen {
    // Auth
    object Login : Screen()

    // Main tabs
    object Dashboard : Screen()
    object Search : Screen()
    object Revenue : Screen()

    // Customer
    object CustomerList : Screen()
    data class CustomerDetail(val customerId: String) : Screen()
    data class CustomerForm(val customerId: String? = null) : Screen()  // null = add mode

    // Order
    object OrderList : Screen()
    data class OrderDetail(val orderId: String) : Screen()
    data class CreateOrder(val customerId: String? = null) : Screen()

    // Measurement
    data class MeasurementForm(val customerId: String, val measurementId: String? = null) : Screen()

    // Tailor
    object TailorList : Screen()
    data class TailorOrders(val tailorId: String) : Screen()
    object TailorForm : Screen()

    // Profile
    object Profile : Screen()
}
