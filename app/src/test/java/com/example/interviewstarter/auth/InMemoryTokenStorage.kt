package com.example.interviewstarter.data.auth

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryTokenStorage : TokenStorage {
    private val state = MutableStateFlow<AuthTokens?>(null)
    override val tokens = state.asStateFlow()
    override suspend fun save(tokens: AuthTokens) { state.value = tokens }
    override suspend fun clear() { state.value = null }
    override suspend fun get(): AuthTokens? = state.value
}
