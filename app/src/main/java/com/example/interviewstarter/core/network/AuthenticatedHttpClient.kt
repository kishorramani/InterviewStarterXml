package com.example.interviewstarter.core.network

import com.example.interviewstarter.data.auth.TokenStorage
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createAuthenticatedHttpClient(
    tokenStorage: TokenStorage,
    refresh: suspend (String) -> com.example.interviewstarter.data.auth.AuthTokens?
): HttpClient =
    HttpClient(CIO) {
        expectSuccess = false

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                explicitNulls = false
            })
        }

        install(Logging) {
            level = LogLevel.HEADERS
        }

        install(Auth) {
            bearer {
                loadTokens {
                    tokenStorage.get()?.let {
                        BearerTokens(it.accessToken, it.refreshToken)
                    }
                }

                refreshTokens {
                    val refreshToken =
                        oldTokens?.refreshToken ?: return@refreshTokens null

                    refresh(refreshToken)?.let {
                        BearerTokens(it.accessToken, it.refreshToken)
                    }
                }
            }
        }
    }
