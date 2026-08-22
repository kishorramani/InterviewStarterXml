package com.example.interviewstarter.presentation.feed

import com.example.interviewstarter.domain.model.Article
import com.example.interviewstarter.domain.model.Category

data class FeedUiState(
    val articles: List<Article> = emptyList(),
    val selectedCategory: Category = Category.ALL,
    val searchQuery: String = "",
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

sealed interface FeedUiEffect {
    data class ShowSnackbar(val message: String) : FeedUiEffect
    data class NavigateToDetail(val articleId: String) : FeedUiEffect
}
