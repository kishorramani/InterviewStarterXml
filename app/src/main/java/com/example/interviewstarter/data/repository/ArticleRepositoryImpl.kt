package com.example.interviewstarter.data.repository

import com.example.interviewstarter.data.local.dao.ArticleDao
import com.example.interviewstarter.data.local.mapper.toDomain
import com.example.interviewstarter.data.local.mapper.toEntity
import com.example.interviewstarter.data.remote.api.ArticleApiService
import com.example.interviewstarter.domain.model.Article
import com.example.interviewstarter.domain.model.Category
import com.example.interviewstarter.domain.repository.ArticleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

class ArticleRepositoryImpl(
    private val apiService: ArticleApiService,
    private val articleDao: ArticleDao
) : ArticleRepository {

    override fun getArticles(category: Category, forceRefresh: Boolean): Flow<List<Article>> {
        val flow = if (category == Category.ALL) {
            articleDao.getAllArticles()
        } else {
            articleDao.getArticlesByCategory(category.tag)
        }

        return flow.map { entities -> entities.map { it.toDomain() } }
            .onStart {
                try {
                    refreshArticles()
                } catch (e: Exception) {
                    // Suppress network errors; offline-first Room cache serves seamlessly
                }
            }
            .flowOn(Dispatchers.IO)
    }

    override fun getArticleById(id: String): Flow<Article?> {
        return articleDao.getArticleById(id)
            .map { it?.toDomain() }
            .flowOn(Dispatchers.IO)
    }

    override fun searchArticles(query: String): Flow<List<Article>> {
        return articleDao.searchArticles(query)
            .map { entities -> entities.map { it.toDomain() } }
            .flowOn(Dispatchers.IO)
    }

    override fun getBookmarkedArticles(): Flow<List<Article>> {
        return articleDao.getBookmarkedArticles()
            .map { entities -> entities.map { it.toDomain() } }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun toggleBookmark(articleId: String): Boolean = withContext(Dispatchers.IO) {
        articleDao.toggleBookmark(articleId)
    }

    override suspend fun refreshArticles() = withContext(Dispatchers.IO) {
        val articles = try {
            val dtos = apiService.getArticles(perPage = 30)
            if (dtos.isEmpty()) ArticleApiService.getMockArticles() else dtos
        } catch (e: Exception) {
            // Use mock fallback when remote fails or is unavailable
            ArticleApiService.getMockArticles()
        }

        val domainArticles = articles.map { it.toDomain() }
        
        // Preserve bookmark states of previously saved articles
        val entitiesToSave = domainArticles.map { domainArticle ->
            val isCurrentlyBookmarked = articleDao.isBookmarked(domainArticle.id) ?: false
            domainArticle.copy(isBookmarked = isCurrentlyBookmarked).toEntity()
        }

        articleDao.insertArticles(entitiesToSave)
    }
}
