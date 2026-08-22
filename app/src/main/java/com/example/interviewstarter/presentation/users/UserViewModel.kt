package com.example.interviewstarter.presentation.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.interviewstarter.core.network.NetworkResult
import com.example.interviewstarter.domain.usecase.GetPagedUsersUseCase
import com.example.interviewstarter.domain.usecase.GetUsersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val getUsers: GetUsersUseCase,
    getPagedUsers: GetPagedUsersUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(UserUiState())
    val uiState = _uiState.asStateFlow()
    val pagedUsers = getPagedUsers().cachedIn(viewModelScope)

    fun loadUsers() {
        viewModelScope.launch {
            _uiState.value = UserUiState(isLoading = true)
            _uiState.value = when (val result = getUsers()) {
                is NetworkResult.Success -> UserUiState(users = result.data)
                is NetworkResult.HttpError ->
                    UserUiState(error = "HTTP ${result.code}: ${result.message}")
                is NetworkResult.NetworkError ->
                    UserUiState(error = result.message)
                is NetworkResult.UnknownError ->
                    UserUiState(error = result.message)
            }
        }
    }
}
