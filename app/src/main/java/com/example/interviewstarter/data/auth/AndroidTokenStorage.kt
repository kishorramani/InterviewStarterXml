package com.example.interviewstarter.data.auth

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.authDataStore by preferencesDataStore("auth_tokens")

class AndroidTokenStorage(
    private val context: Context
) : TokenStorage {

    private val accessKey = stringPreferencesKey("access_token")
    private val refreshKey = stringPreferencesKey("refresh_token")

    override val tokens: Flow<AuthTokens?> =
        combine(
            context.authDataStore.data.map { it[accessKey] },
            context.authDataStore.data.map { it[refreshKey] }
        ) { access, refresh ->
            if (access != null && refresh != null) AuthTokens(access, refresh)
            else null
        }

    override suspend fun save(tokens: AuthTokens) {
        context.authDataStore.edit {
            it[accessKey] = tokens.accessToken
            it[refreshKey] = tokens.refreshToken
        }
    }

    override suspend fun clear() {
        context.authDataStore.edit {
            it.remove(accessKey)
            it.remove(refreshKey)
        }
    }

    override suspend fun get(): AuthTokens? = tokens.first()
}
