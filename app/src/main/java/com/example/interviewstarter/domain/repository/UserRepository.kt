package com.example.interviewstarter.domain.repository

import androidx.paging.PagingData
import com.example.interviewstarter.core.network.NetworkResult
import com.example.interviewstarter.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUsers(): NetworkResult<List<User>>
    fun observeUsers(): Flow<List<User>>
    fun searchUsers(query: String): Flow<List<User>>
    fun pagedUsers(): Flow<PagingData<User>>
}
