package com.example.interviewstarter.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val summary: String,
    val content: String,
    val url: String,
    val imageUrl: String,
    val categoryTag: String,
    val author: String,
    val publishedAt: String,
    val readTimeMinutes: Int,
    val tagsRaw: String,
    val isBookmarked: Boolean = false,
    val cachedAt: Long = System.currentTimeMillis()
)
