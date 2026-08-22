package com.example.interviewstarter.data.local.mapper

import com.example.interviewstarter.data.local.entity.ArticleEntity
import com.example.interviewstarter.domain.model.Article
import com.example.interviewstarter.domain.model.Category

fun ArticleEntity.toDomain(): Article {
    return Article(
        id = id,
        title = title,
        summary = summary,
        content = content,
        url = url,
        imageUrl = imageUrl,
        category = Category.fromTag(categoryTag),
        author = author,
        publishedAt = publishedAt,
        readTimeMinutes = readTimeMinutes,
        tags = if (tagsRaw.isBlank()) emptyList() else tagsRaw.split(",").map { it.trim() },
        isBookmarked = isBookmarked
    )
}

fun Article.toEntity(): ArticleEntity {
    return ArticleEntity(
        id = id,
        title = title,
        summary = summary,
        content = content,
        url = url,
        imageUrl = imageUrl,
        categoryTag = category.tag,
        author = author,
        publishedAt = publishedAt,
        readTimeMinutes = readTimeMinutes,
        tagsRaw = tags.joinToString(","),
        isBookmarked = isBookmarked,
        cachedAt = System.currentTimeMillis()
    )
}
