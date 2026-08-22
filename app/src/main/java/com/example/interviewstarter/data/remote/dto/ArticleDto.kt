package com.example.interviewstarter.data.remote.dto

import com.example.interviewstarter.domain.model.Article
import com.example.interviewstarter.domain.model.Category
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArticleDto(
    @Json(name = "id") val id: Long,
    @Json(name = "title") val title: String,
    @Json(name = "description") val description: String? = null,
    @Json(name = "body_markdown") val bodyMarkdown: String? = null,
    @Json(name = "url") val url: String? = null,
    @Json(name = "cover_image") val coverImage: String? = null,
    @Json(name = "social_image") val socialImage: String? = null,
    @Json(name = "readable_publish_date") val readablePublishDate: String? = null,
    @Json(name = "reading_time_minutes") val readingTimeMinutes: Int? = 5,
    @Json(name = "tag_list") val tagList: List<String>? = emptyList(),
    @Json(name = "tags") val tags: String? = null,
    @Json(name = "user") val user: UserDto? = null,
    @Json(name = "published_at") val publishedAt: String? = null
) {
    @JsonClass(generateAdapter = true)
    data class UserDto(
        @Json(name = "name") val name: String? = null,
        @Json(name = "username") val username: String? = null,
        @Json(name = "profile_image") val profileImage: String? = null
    )

    fun toDomain(): Article {
        val extractedTags = tagList?.takeIf { it.isNotEmpty() }
            ?: tags?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() }
            ?: emptyList()

        val primaryCategory = when {
            extractedTags.any { it.contains("ai", ignoreCase = true) || it.contains("ml", ignoreCase = true) || it.contains("gemini", ignoreCase = true) } -> Category.AI_ML
            extractedTags.any { it.contains("mobile", ignoreCase = true) || it.contains("android", ignoreCase = true) || it.contains("kmp", ignoreCase = true) || it.contains("kotlin", ignoreCase = true) || it.contains("compose", ignoreCase = true) } -> Category.MOBILE
            extractedTags.any { it.contains("devops", ignoreCase = true) || it.contains("cloud", ignoreCase = true) || it.contains("docker", ignoreCase = true) || it.contains("k8s", ignoreCase = true) } -> Category.DEVOPS
            extractedTags.any { it.contains("web", ignoreCase = true) || it.contains("react", ignoreCase = true) || it.contains("wasm", ignoreCase = true) || it.contains("js", ignoreCase = true) } -> Category.WEB
            else -> Category.ALL
        }

        val resolvedImageUrl = coverImage
            ?: socialImage
            ?: "https://picsum.photos/seed/${id}/800/450"

        val resolvedContent = bodyMarkdown?.takeIf { it.isNotBlank() }
            ?: description?.takeIf { it.isNotBlank() }
            ?: title

        return Article(
            id = id.toString(),
            title = title,
            summary = description ?: "Explore detailed insights and developer best practices in this article.",
            content = resolvedContent,
            url = url ?: "https://dev.to",
            imageUrl = resolvedImageUrl,
            category = primaryCategory,
            author = user?.name ?: "Tech Contributor",
            publishedAt = readablePublishDate ?: publishedAt ?: "Recently",
            readTimeMinutes = readingTimeMinutes ?: 5,
            tags = extractedTags,
            isBookmarked = false
        )
    }
}
