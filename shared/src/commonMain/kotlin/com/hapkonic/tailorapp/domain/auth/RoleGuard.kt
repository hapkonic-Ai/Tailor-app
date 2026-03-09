package com.hapkonic.tailorapp.domain.auth

import com.hapkonic.tailorapp.domain.model.AppUser
import com.hapkonic.tailorapp.domain.model.Order
import com.hapkonic.tailorapp.domain.model.UserRole

/**
 * Centralised RBAC checks.
 * All permission decisions flow through here so UI and use cases share the same logic.
 */
class RoleGuard {

    /** Throws [UnauthorizedException] if the user is not an ADMIN. */
    fun requireAdmin(user: AppUser?) {
        if (user == null || user.role != UserRole.ADMIN) {
            throw UnauthorizedException("Admin role required.")
        }
    }

    /** Throws [UnauthorizedException] if the user is neither ADMIN nor TAILOR. */
    fun requireTailor(user: AppUser?) {
        if (user == null) throw UnauthorizedException("Authentication required.")
    }

    /**
     * Returns true if [user] may create / update / delete [order].
     * - Admins can touch any order.
     * - Tailors can only touch orders assigned to them.
     */
    fun canModifyOrder(user: AppUser?, order: Order): Boolean {
        if (user == null) return false
        return user.role == UserRole.ADMIN || order.assignedTailorId == user.uid
    }

    /** Returns true if [user] may view financial / analytics screens. */
    fun canViewAnalytics(user: AppUser?): Boolean =
        user?.role == UserRole.ADMIN
}

class UnauthorizedException(message: String) : Exception(message)
