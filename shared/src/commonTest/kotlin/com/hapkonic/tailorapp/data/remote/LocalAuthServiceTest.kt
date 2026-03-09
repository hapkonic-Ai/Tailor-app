package com.hapkonic.tailorapp.data.remote

import com.hapkonic.tailorapp.domain.model.UserRole
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class LocalAuthServiceTest {

    private val service = LocalAuthService()

    @Test
    fun `valid admin credentials return ADMIN user`() {
        val user = service.signIn("admin@tailorapp.com", "admin123")
        assertEquals(UserRole.ADMIN, user.role)
        assertEquals("admin@tailorapp.com", user.email)
    }

    @Test
    fun `valid tailor credentials return TAILOR user`() {
        val user = service.signIn("tailor@tailorapp.com", "tailor123")
        assertEquals(UserRole.TAILOR, user.role)
    }

    @Test
    fun `email is case-insensitive`() {
        val user = service.signIn("ADMIN@TAILORAPP.COM", "admin123")
        assertEquals(UserRole.ADMIN, user.role)
    }

    @Test
    fun `wrong password throws AuthException`() {
        assertFailsWith<AuthException> {
            service.signIn("admin@tailorapp.com", "wrongpassword")
        }
    }

    @Test
    fun `unknown email throws AuthException`() {
        assertFailsWith<AuthException> {
            service.signIn("nobody@example.com", "any")
        }
    }

    @Test
    fun `signIn sets current user`() {
        service.signIn("admin@tailorapp.com", "admin123")
        assertNotNull(service.getCurrentUser())
    }

    @Test
    fun `signOut clears current user`() {
        service.signIn("admin@tailorapp.com", "admin123")
        service.signOut()
        assertNull(service.getCurrentUser())
    }

    @Test
    fun `getCurrentUser returns null before sign in`() {
        assertNull(service.getCurrentUser())
    }
}
