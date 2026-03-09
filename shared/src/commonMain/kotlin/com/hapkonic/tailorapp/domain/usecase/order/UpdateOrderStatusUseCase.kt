package com.hapkonic.tailorapp.domain.usecase.order

import com.hapkonic.tailorapp.data.sync.currentTimeMillis
import com.hapkonic.tailorapp.domain.model.AppUser
import com.hapkonic.tailorapp.domain.model.Order
import com.hapkonic.tailorapp.domain.model.OrderStatus
import com.hapkonic.tailorapp.domain.model.UserRole
import com.hapkonic.tailorapp.domain.repository.OrderRepository

class UpdateOrderStatusUseCase(private val repo: OrderRepository) {
    /**
     * Updates order status with role enforcement:
     * - Admin can set any status.
     * - Tailor can only advance to the next status on their own orders.
     * @throws IllegalStateException on permission violation.
     */
    suspend operator fun invoke(order: Order, newStatus: OrderStatus, actor: AppUser) {
        if (actor.role == UserRole.TAILOR) {
            if (order.assignedTailorId != actor.uid)
                throw IllegalStateException("You can only update your own orders.")
            if (order.status.next() != newStatus)
                throw IllegalStateException("Tailors may only advance to the next status.")
        }
        repo.updateStatus(order.id, newStatus, currentTimeMillis())
    }
}
