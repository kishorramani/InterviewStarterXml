package com.example.interviewstarter.data.auth

import com.example.interviewstarter.core.network.NetworkResult

interface AuthRepository {
    suspend fun login(email: String, password: String): NetworkResult<AuthTokens>
    suspend fun refresh(refreshToken: String): AuthTokens?
    suspend fun logout()
}
