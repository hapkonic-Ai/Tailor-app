package com.hapkonic.tailorapp.domain.usecase

import com.hapkonic.tailorapp.domain.model.AppUser
import com.hapkonic.tailorapp.domain.model.Order
import com.hapkonic.tailorapp.domain.model.OrderStatus
import com.hapkonic.tailorapp.domain.model.UserRole
import com.hapkonic.tailorapp.domain.usecase.order.UpdateOrderStatusUseCase
import com.hapkonic.tailorapp.fakes.FakeOrderRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class UpdateOrderStatusUseCaseTest {

    private val repo = FakeOrderRepository()
    private val useCase = UpdateOrderStatusUseCase(repo)

    private val admin = AppUser(uid = "admin1", email = "admin@tailorapp.com", role = UserRole.ADMIN)
    private val tailor = AppUser(uid = "tailor1", email = "tailor@tailorapp.com", role = UserRole.TAILOR)

    private fun makeOrder(
        id: String = "o1",
        status: OrderStatus = OrderStatus.PENDING,
        tailorId: String = "tailor1"
    ) = Order(
        id               = id,
        customerId       = "cust1",
        orderDate        = 1_000L,
        deliveryDate     = 2_000L,
        status           = status,
        assignedTailorId = tailorId,
        price            = 500.0,
        clothImageUrl    = null,
        designImageUrl   = null,
        notes            = null,
        updatedAt        = 1_000L
    )

    @Test
    fun `admin can set any status`() = runTest {
        val order = makeOrder(status = OrderStatus.PENDING)
        useCase(order, OrderStatus.DELIVERED, admin)
        assertEquals(OrderStatus.DELIVERED, repo.statusUpdates.last().second)
    }

    @Test
    fun `tailor can advance own order to next status`() = runTest {
        val order = makeOrder(status = OrderStatus.PENDING, tailorId = tailor.uid)
        useCase(order, OrderStatus.IN_PROGRESS, tailor)
        assertEquals(OrderStatus.IN_PROGRESS, repo.statusUpdates.last().second)
    }

    @Test
    fun `tailor cannot update another tailors order`() = runTest {
        val otherTailor = AppUser(uid = "other", email = "other@tailorapp.com", role = UserRole.TAILOR)
        val order = makeOrder(tailorId = "tailor1")
        assertFailsWith<IllegalStateException> {
            useCase(order, OrderStatus.IN_PROGRESS, otherTailor)
        }
    }

    @Test
    fun `tailor cannot skip status`() = runTest {
        val order = makeOrder(status = OrderStatus.PENDING, tailorId = tailor.uid)
        assertFailsWith<IllegalStateException> {
            useCase(order, OrderStatus.READY, tailor)
        }
    }

    @Test
    fun `tailor cannot go back to previous status`() = runTest {
        val order = makeOrder(status = OrderStatus.IN_PROGRESS, tailorId = tailor.uid)
        assertFailsWith<IllegalStateException> {
            useCase(order, OrderStatus.PENDING, tailor)
        }
    }

    @Test
    fun `updateStatus called with correct orderId`() = runTest {
        val order = makeOrder(id = "order-xyz", status = OrderStatus.PENDING)
        useCase(order, OrderStatus.READY, admin)
        assertEquals("order-xyz", repo.statusUpdates.last().first)
    }

    @Test
    fun `no repo call on permission failure`() = runTest {
        val order = makeOrder(tailorId = "someone-else")
        runCatching { useCase(order, OrderStatus.IN_PROGRESS, tailor) }
        assertTrue(repo.statusUpdates.isEmpty())
    }
}
