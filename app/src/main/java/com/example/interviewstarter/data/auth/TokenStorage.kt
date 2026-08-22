package com.example.interviewstarter.data.auth

import kotlinx.coroutines.flow.Flow

interface TokenStorage {
    val tokens: Flow<AuthTokens?>
    suspend fun save(tokens: AuthTokens)
    suspend fun clear()
    suspend fun get(): AuthTokens?
}
