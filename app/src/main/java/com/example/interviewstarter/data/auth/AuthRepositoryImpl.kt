package com.example.interviewstarter.data.auth

import com.example.interviewstarter.core.network.NetworkResult

class AuthRepositoryImpl(
    private val api: FakeAuthApi,
    private val storage: TokenStorage
) : AuthRepository {

    override suspend fun login(
        email: String,
        password: String
    ): NetworkResult<AuthTokens> = runCatching {
        api.login(email, password)
    }.fold(
        onSuccess = { tokens ->
            storage.save(tokens)
            NetworkResult.Success(tokens)
        },
        onFailure = { error ->
            NetworkResult.UnknownError(
                error.message ?: "Login failed",
                error
            )
        }
    )

    override suspend fun refresh(refreshToken: String): AuthTokens? =
        api.refresh(refreshToken)?.also { storage.save(it) }

    override suspend fun logout() {
        storage.clear()
    }
}
