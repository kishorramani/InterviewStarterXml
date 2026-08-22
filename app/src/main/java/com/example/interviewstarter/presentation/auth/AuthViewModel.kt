package com.example.interviewstarter.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interviewstarter.core.network.NetworkResult
import com.example.interviewstarter.data.auth.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val error: String? = null
)

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            _uiState.value = when (val result = repository.login(email, password)) {
                is NetworkResult.Success ->
                    AuthUiState(isLoggedIn = true)
                is NetworkResult.HttpError ->
                    AuthUiState(error = "HTTP ${result.code}: ${result.message}")
                is NetworkResult.NetworkError ->
                    AuthUiState(error = result.message)
                is NetworkResult.UnknownError ->
                    AuthUiState(error = result.message)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _uiState.value = AuthUiState()
        }
    }
}
