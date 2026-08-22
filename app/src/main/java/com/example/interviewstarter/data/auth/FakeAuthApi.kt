package com.example.interviewstarter.data.auth

/**
 * Interview-safe fake auth backend.
 * Replace with the real endpoint/DTO in a production app.
 */
class FakeAuthApi {
    suspend fun login(email: String, password: String): AuthTokens {
        if (email.isBlank() || password.isBlank()) {
            error("Email/password required")
        }
        return AuthTokens(
            accessToken = "access-${email.hashCode()}",
            refreshToken = "refresh-${email.hashCode()}"
        )
    }

    suspend fun refresh(refreshToken: String): AuthTokens? {
        if (refreshToken.isBlank()) return null
        return AuthTokens(
            accessToken = "access-refreshed-${refreshToken.hashCode()}",
            refreshToken = refreshToken
        )
    }
}
