package com.example.interviewstarter.domain.usecase

import com.example.interviewstarter.domain.model.Article
import com.example.interviewstarter.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow

class GetBookmarkedArticlesUseCase(
    private val repository: ArticleRepository
) {
    operator fun invoke(): Flow<List<Article>> {
        return repository.getBookmarkedArticles()
    }
}
