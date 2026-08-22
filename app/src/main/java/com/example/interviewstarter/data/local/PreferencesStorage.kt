package com.example.interviewstarter.data.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("app_preferences")

class PreferencesStorage(private val context: Context) {
    private val stringKey = stringPreferencesKey("username")
    private val intKey = intPreferencesKey("user_id")
    private val longKey = longPreferencesKey("count")
    private val floatKey = floatPreferencesKey("rating")
    private val doubleKey = doublePreferencesKey("balance")
    private val boolKey = booleanPreferencesKey("logged_in")

    val username: Flow<String?> = context.dataStore.data.map { it[stringKey] }
    val userId: Flow<Int?> = context.dataStore.data.map { it[intKey] }
    val count: Flow<Long?> = context.dataStore.data.map { it[longKey] }
    val rating: Flow<Float?> = context.dataStore.data.map { it[floatKey] }
    val balance: Flow<Double?> = context.dataStore.data.map { it[doubleKey] }
    val isLoggedIn: Flow<Boolean?> = context.dataStore.data.map { it[boolKey] }

    suspend fun saveUsername(value: String) =
        context.dataStore.edit { it[stringKey] = value }

    suspend fun saveUserId(value: Int) =
        context.dataStore.edit { it[intKey] = value }

    suspend fun saveCount(value: Long) =
        context.dataStore.edit { it[longKey] = value }

    suspend fun saveRating(value: Float) =
        context.dataStore.edit { it[floatKey] = value }

    suspend fun saveBalance(value: Double) =
        context.dataStore.edit { it[doubleKey] = value }

    suspend fun saveLoggedIn(value: Boolean) =
        context.dataStore.edit { it[boolKey] = value }

    suspend fun clear() =
        context.dataStore.edit { it.clear() }
}
