package com.example.interviewstarter.presentation.detail

import com.example.interviewstarter.domain.model.Article

data class DetailUiState(
    val article: Article? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

sealed interface DetailUiEffect {
    data class ShowSnackbar(val message: String) : DetailUiEffect
    data class OpenBrowser(val url: String) : DetailUiEffect
    data class ShareArticle(val title: String, val url: String) : DetailUiEffect
}
