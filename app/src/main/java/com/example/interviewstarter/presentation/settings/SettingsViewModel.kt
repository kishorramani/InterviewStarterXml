package com.example.interviewstarter.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interviewstarter.domain.usecase.GetPlatformMetricsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val getPlatformMetricsUseCase: GetPlatformMetricsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<SettingsUiEffect>()
    val uiEffect: SharedFlow<SettingsUiEffect> = _uiEffect.asSharedFlow()

    init {
        loadPlatformMetrics()
    }

    fun loadPlatformMetrics() {
        val info = getPlatformMetricsUseCase()
        _uiState.update { it.copy(platformInfo = info) }
    }

    fun toggleDarkMode(enabled: Boolean) {
        _uiState.update { it.copy(isDarkMode = enabled) }
    }

    fun clearCache() {
        viewModelScope.launch {
            _uiState.update { it.copy(isClearingCache = true) }
            getPlatformMetricsUseCase.clearCache()
            _uiState.update { it.copy(isClearingCache = false) }
            _uiEffect.emit(SettingsUiEffect.ShowSnackbar("Local cache cleared successfully!"))
        }
    }
}
