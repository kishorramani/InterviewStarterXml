package com.example.interviewstarter.presentation.users

import com.example.interviewstarter.domain.model.User

data class UserUiState(
    val isLoading: Boolean = false,
    val users: List<User> = emptyList(),
    val error: String? = null
)
