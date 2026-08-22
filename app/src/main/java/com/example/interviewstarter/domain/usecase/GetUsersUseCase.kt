package com.example.interviewstarter.domain.usecase

import com.example.interviewstarter.domain.repository.UserRepository

class GetUsersUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke() = repository.getUsers()
}
