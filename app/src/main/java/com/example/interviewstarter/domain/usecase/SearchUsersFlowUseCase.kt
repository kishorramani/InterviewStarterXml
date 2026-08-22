package com.example.interviewstarter.domain.usecase

import com.example.interviewstarter.domain.model.User
import com.example.interviewstarter.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest

class SearchUsersFlowUseCase(
    private val repository: UserRepository
) {
    operator fun invoke(query: Flow<String>): Flow<List<User>> =
        query
            .debounce(450)
            .distinctUntilChanged()
            .flatMapLatest(repository::searchUsers)
}
