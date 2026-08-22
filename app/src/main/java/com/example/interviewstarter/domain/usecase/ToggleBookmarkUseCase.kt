package com.example.interviewstarter.domain.usecase

import com.example.interviewstarter.domain.repository.ArticleRepository

class ToggleBookmarkUseCase(
    private val repository: ArticleRepository
) {
    suspend operator fun invoke(articleId: String): Boolean {
        return repository.toggleBookmark(articleId)
    }
}
