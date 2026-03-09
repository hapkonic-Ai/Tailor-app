package com.hapkonic.tailorapp.domain.usecase.dashboard

import com.hapkonic.tailorapp.domain.model.Order
import com.hapkonic.tailorapp.domain.model.OrderStatus
import com.hapkonic.tailorapp.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class RevenueReport(
    val totalRevenue: Double = 0.0,
    val orderCount: Int = 0,
    val byStatus: Map<OrderStatus, Double> = emptyMap(),
    val byDay: List<DayRevenue> = emptyList()
)

data class DayRevenue(val dateMillis: Long, val revenue: Double, val orderCount: Int)

class GetRevenueReportUseCase(private val repo: OrderRepository) {
    operator fun invoke(startDate: Long, endDate: Long): Flow<RevenueReport> =
        repo.getOrdersInDateRange(startDate, endDate).map { orders ->
            buildReport(orders)
        }

    private fun buildReport(orders: List<Order>): RevenueReport {
        val total    = orders.sumOf { it.price }
        val byStatus = orders.groupBy { it.status }
                             .mapValues { (_, list) -> list.sumOf { it.price } }

        // Group by calendar day (bucket by day = 86_400_000 ms)
        val byDay = orders.groupBy { it.orderDate / 86_400_000L }
            .map { (dayKey, list) ->
                DayRevenue(
                    dateMillis = dayKey * 86_400_000L,
                    revenue    = list.sumOf { it.price },
                    orderCount = list.size
                )
            }
            .sortedBy { it.dateMillis }

        return RevenueReport(
            totalRevenue = total,
            orderCount   = orders.size,
            byStatus     = byStatus,
            byDay        = byDay
        )
    }
}
