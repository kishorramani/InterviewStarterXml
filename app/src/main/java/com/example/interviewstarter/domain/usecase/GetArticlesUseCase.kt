package com.example.interviewstarter.domain.usecase

import com.example.interviewstarter.domain.model.Article
import com.example.interviewstarter.domain.model.Category
import com.example.interviewstarter.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow

class GetArticlesUseCase(
    private val repository: ArticleRepository
) {
    operator fun invoke(category: Category = Category.ALL, forceRefresh: Boolean = false): Flow<List<Article>> {
        return repository.getArticles(category, forceRefresh)
    }

    fun getById(id: String): Flow<Article?> {
        return repository.getArticleById(id)
    }
}
