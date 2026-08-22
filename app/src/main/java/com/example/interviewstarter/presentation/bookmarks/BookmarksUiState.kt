package com.example.interviewstarter.presentation.bookmarks

import com.example.interviewstarter.domain.model.Article

data class BookmarksUiState(
    val bookmarkedArticles: List<Article> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

sealed interface BookmarksUiEffect {
    data class ShowSnackbar(val message: String) : BookmarksUiEffect
    data class NavigateToDetail(val articleId: String) : BookmarksUiEffect
}
