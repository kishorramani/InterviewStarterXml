package com.example.interviewstarter.auth

import com.example.interviewstarter.data.auth.AuthRepositoryImpl
import com.example.interviewstarter.data.auth.FakeAuthApi
import com.example.interviewstarter.data.auth.InMemoryTokenStorage
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Test

class AuthRepositoryTest {
    @Test
    fun loginStoresTokens() = runTest {
        val storage = InMemoryTokenStorage()
        val repository = AuthRepositoryImpl(FakeAuthApi(), storage)

        repository.login("dev@example.com", "password")

        assertNotNull(storage.get())
    }
}
