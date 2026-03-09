package com.hapkonic.tailorapp.domain.usecase.dashboard

import com.hapkonic.tailorapp.domain.model.OrderStatus
import com.hapkonic.tailorapp.domain.model.Tailor
import com.hapkonic.tailorapp.domain.repository.OrderRepository
import com.hapkonic.tailorapp.domain.repository.TailorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

data class DashboardMetrics(
    val pendingCount: Long = 0,
    val inProgressCount: Long = 0,
    val readyCount: Long = 0,
    val deliveredTodayCount: Long = 0,
    val tailorWorkload: List<TailorWorkload> = emptyList(),
    val totalActiveOrders: Long = 0
)

data class TailorWorkload(val tailorName: String, val activeOrderCount: Int)

class GetDashboardMetricsUseCase(
    private val orderRepo: OrderRepository,
    private val tailorRepo: TailorRepository
) {
    operator fun invoke(): Flow<DashboardMetrics> = combine(
        orderRepo.getCountByStatus(),
        tailorRepo.getAll()
    ) { countMap, tailors ->
        val pending     = countMap[OrderStatus.PENDING]     ?: 0L
        val inProgress  = countMap[OrderStatus.IN_PROGRESS] ?: 0L
        val ready       = countMap[OrderStatus.READY]       ?: 0L
        val delivered   = countMap[OrderStatus.DELIVERED]   ?: 0L

        DashboardMetrics(
            pendingCount        = pending,
            inProgressCount     = inProgress,
            readyCount          = ready,
            deliveredTodayCount = delivered,
            tailorWorkload      = tailors.map { TailorWorkload(it.name, it.activeOrders) }
                                         .sortedByDescending { it.activeOrderCount },
            totalActiveOrders   = pending + inProgress + ready
        )
    }
}
