package com.example.interviewstarter.core.ui

data class PagingUiState(
    val isRefreshing: Boolean = false,
    val isAppending: Boolean = false,
    val error: String? = null
)
