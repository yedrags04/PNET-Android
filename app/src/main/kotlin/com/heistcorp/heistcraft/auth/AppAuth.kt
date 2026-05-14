package com.heistcorp.heistcraft.auth

import androidx.compose.runtime.mutableStateOf

/**
 * Sesión local (demo). Las cuentas viven en memoria hasta cerrar la app.
 * Sustituir por API / DataStore en producción.
 */
object AppAuth {
    private val sessionState = mutableStateOf<UserSession?>(null)
    private val accounts = mutableMapOf<String, UserSession>()

    val currentSession: UserSession?
        get() = sessionState.value

    fun register(session: UserSession): Boolean {
        val key = session.email.trim().lowercase()
        if (accounts.containsKey(key)) return false
        accounts[key] = session.copy(email = key)
        sessionState.value = accounts[key]
        return true
    }

    fun signIn(email: String, password: String): Boolean {
        val key = email.trim().lowercase()
        val stored = accounts[key] ?: return false
        if (stored.password != password) return false
        sessionState.value = stored
        return true
    }

    fun signOut() {
        sessionState.value = null
    }
}
