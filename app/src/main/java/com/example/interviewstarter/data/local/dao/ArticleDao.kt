package com.example.interviewstarter.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.interviewstarter.data.local.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Query("SELECT * FROM articles ORDER BY cachedAt DESC")
    fun getAllArticles(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE categoryTag = :categoryTag ORDER BY cachedAt DESC")
    fun getArticlesByCategory(categoryTag: String): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE id = :id LIMIT 1")
    fun getArticleById(id: String): Flow<ArticleEntity?>

    @Query("SELECT * FROM articles WHERE isBookmarked = 1 ORDER BY cachedAt DESC")
    fun getBookmarkedArticles(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE title LIKE '%' || :query || '%' OR summary LIKE '%' || :query || '%' OR tagsRaw LIKE '%' || :query || '%' ORDER BY cachedAt DESC")
    fun searchArticles(query: String): Flow<List<ArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<ArticleEntity>)

    @Query("UPDATE articles SET isBookmarked = :isBookmarked WHERE id = :id")
    suspend fun updateBookmarkStatus(id: String, isBookmarked: Boolean)

    @Query("SELECT isBookmarked FROM articles WHERE id = :id LIMIT 1")
    suspend fun isBookmarked(id: String): Boolean?

    @Transaction
    suspend fun toggleBookmark(id: String): Boolean {
        val currentStatus = isBookmarked(id) ?: false
        val newStatus = !currentStatus
        updateBookmarkStatus(id, newStatus)
        return newStatus
    }

    @Query("DELETE FROM articles WHERE isBookmarked = 0")
    suspend fun clearNonBookmarkedArticles()

    @Query("DELETE FROM articles")
    suspend fun clearAll()
}
