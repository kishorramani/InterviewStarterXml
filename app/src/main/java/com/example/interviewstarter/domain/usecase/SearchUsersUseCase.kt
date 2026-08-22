package com.example.interviewstarter.domain.usecase

import com.example.interviewstarter.domain.model.User
import com.example.interviewstarter.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class SearchUsersUseCase(private val repository: UserRepository) {
    operator fun invoke(query: String): Flow<List<User>> =
        repository.searchUsers(query)
}
