package com.example.interviewstarter.presentation.settings

import com.example.interviewstarter.domain.model.PlatformInfo

data class SettingsUiState(
    val platformInfo: PlatformInfo? = null,
    val isDarkMode: Boolean = false,
    val isClearingCache: Boolean = false
)

sealed interface SettingsUiEffect {
    data class ShowSnackbar(val message: String) : SettingsUiEffect
}
