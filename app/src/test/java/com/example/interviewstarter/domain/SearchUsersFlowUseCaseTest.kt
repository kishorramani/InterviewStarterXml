package com.example.interviewstarter.domain

import com.example.interviewstarter.domain.model.User
import com.example.interviewstarter.domain.repository.UserRepository
import com.example.interviewstarter.domain.usecase.SearchUsersFlowUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchUsersFlowUseCaseTest {

    @Test
    fun searchFlowReturnsRepositoryResult() = runTest {
        val users = listOf(
            User(1, "Alice", "alice", "alice@example.com")
        )

        val repository = object : UserRepository {
            override suspend fun getUsers() =
                com.example.interviewstarter.core.network.NetworkResult.Success(users)

            override fun observeUsers(): Flow<List<User>> = flowOf(users)

            override fun searchUsers(query: String): Flow<List<User>> =
                flowOf(users.filter { it.name.contains(query, true) })

            override fun pagedUsers(): Flow<androidx.paging.PagingData<User>> =
                flowOf(androidx.paging.PagingData.empty())
        }

        val query = MutableStateFlow("Ali")
        val result = SearchUsersFlowUseCase(repository)(query)

        kotlinx.coroutines.flow.first(result).let {
            assertEquals("Alice", it.single().name)
        }
    }
}
