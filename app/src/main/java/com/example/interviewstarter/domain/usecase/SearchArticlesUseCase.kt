package com.example.interviewstarter.domain.usecase

import com.example.interviewstarter.domain.model.Article
import com.example.interviewstarter.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow

class SearchArticlesUseCase(
    private val repository: ArticleRepository
) {
    operator fun invoke(query: String): Flow<List<Article>> {
        return repository.searchArticles(query)
    }
}
