package com.hapkonic.tailorapp.domain.usecase

import app.cash.turbine.test
import com.hapkonic.tailorapp.domain.model.Order
import com.hapkonic.tailorapp.domain.model.OrderStatus
import com.hapkonic.tailorapp.domain.usecase.dashboard.GetRevenueReportUseCase
import com.hapkonic.tailorapp.fakes.FakeOrderRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetRevenueReportUseCaseTest {

    private val repo = FakeOrderRepository()
    private val useCase = GetRevenueReportUseCase(repo)

    private val DAY_MS = 86_400_000L

    private fun order(
        id: String,
        status: OrderStatus,
        price: Double,
        orderDate: Long
    ) = Order(
        id               = id,
        customerId       = "c1",
        orderDate        = orderDate,
        deliveryDate     = orderDate + DAY_MS,
        status           = status,
        assignedTailorId = "t1",
        price            = price,
        clothImageUrl    = null,
        designImageUrl   = null,
        notes            = null,
        updatedAt        = orderDate
    )

    @Test
    fun `total revenue sums all prices`() = runTest {
        repo.seed(
            order("o1", OrderStatus.DELIVERED, 500.0, DAY_MS * 1),
            order("o2", OrderStatus.READY,     300.0, DAY_MS * 1),
            order("o3", OrderStatus.PENDING,   200.0, DAY_MS * 2)
        )
        useCase(0L, DAY_MS * 30).test {
            val report = awaitItem()
            assertEquals(1000.0, report.totalRevenue)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `order count matches seeded orders`() = runTest {
        repo.seed(
            order("o1", OrderStatus.DELIVERED, 100.0, DAY_MS),
            order("o2", OrderStatus.PENDING,   200.0, DAY_MS * 2)
        )
        useCase(0L, DAY_MS * 30).test {
            assertEquals(2, awaitItem().orderCount)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `byStatus groups revenue correctly`() = runTest {
        repo.seed(
            order("o1", OrderStatus.DELIVERED, 400.0, DAY_MS),
            order("o2", OrderStatus.DELIVERED, 100.0, DAY_MS),
            order("o3", OrderStatus.PENDING,   200.0, DAY_MS)
        )
        useCase(0L, DAY_MS * 30).test {
            val report = awaitItem()
            assertEquals(500.0, report.byStatus[OrderStatus.DELIVERED])
            assertEquals(200.0, report.byStatus[OrderStatus.PENDING])
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `byDay groups orders into day buckets`() = runTest {
        repo.seed(
            order("o1", OrderStatus.DELIVERED, 100.0, DAY_MS * 1),
            order("o2", OrderStatus.DELIVERED, 200.0, DAY_MS * 1),
            order("o3", OrderStatus.READY,     300.0, DAY_MS * 2)
        )
        useCase(0L, DAY_MS * 30).test {
            val report = awaitItem()
            assertEquals(2, report.byDay.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `empty range returns zeroed report`() = runTest {
        useCase(0L, DAY_MS * 30).test {
            val report = awaitItem()
            assertEquals(0.0, report.totalRevenue)
            assertEquals(0, report.orderCount)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
