package com.example.interviewstarter.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.interviewstarter.core.network.NetworkResult
import com.example.interviewstarter.data.local.dao.UserDao
import com.example.interviewstarter.data.local.mapper.toDomain
import com.example.interviewstarter.data.local.mapper.toEntity
import com.example.interviewstarter.data.remote.api.UserApi
import com.example.interviewstarter.data.remote.mapper.toDomain
import com.example.interviewstarter.domain.model.User
import com.example.interviewstarter.domain.repository.UserRepository
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserRepositoryImpl(
    private val api: UserApi,
    private val dao: UserDao
) : UserRepository {
    override suspend fun getUsers(): NetworkResult<List<User>> =
        runCatching { api.getUsers().map { it.toDomain() } }.fold(
            onSuccess = { users ->
                dao.insertAll(users.map { it.toEntity() })
                NetworkResult.Success(users)
            },
            onFailure = { error ->
                when (error) {
                    is ClientRequestException ->
                        NetworkResult.HttpError(error.response.status.value, "Client error")
                    is ServerResponseException ->
                        NetworkResult.HttpError(error.response.status.value, "Server error")
                    is IOException ->
                        NetworkResult.NetworkError("Network unavailable", error)
                    else ->
                        NetworkResult.UnknownError(error.message ?: "Unknown error", error)
                }
            }
        )

    override fun observeUsers(): Flow<List<User>> =
        dao.observeUsers().map { it.map(UserEntityMapper::toDomain) }

    override fun searchUsers(query: String): Flow<List<User>> =
        dao.searchUsers(query).map { it.map(UserEntityMapper::toDomain) }

    override fun pagedUsers(): Flow<PagingData<User>> =
        Pager(
            PagingConfig(
                pageSize = 20,
                prefetchDistance = 5,
                enablePlaceholders = false
            )
        ) { dao.pagingSource() }
            .flow
            .map { paging -> paging.map(UserEntityMapper::toDomain) }
}

private object UserEntityMapper {
    fun toDomain(entity: com.example.interviewstarter.data.local.entity.UserEntity) =
        entity.toDomain()
}
