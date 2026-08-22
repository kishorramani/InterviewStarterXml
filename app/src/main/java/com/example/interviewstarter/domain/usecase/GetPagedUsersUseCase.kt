package com.example.interviewstarter.domain.usecase

import androidx.paging.PagingData
import com.example.interviewstarter.domain.model.User
import com.example.interviewstarter.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetPagedUsersUseCase(private val repository: UserRepository) {
    operator fun invoke(): Flow<PagingData<User>> = repository.pagedUsers()
}
