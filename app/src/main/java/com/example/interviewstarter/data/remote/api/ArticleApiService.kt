package com.example.interviewstarter.data.remote.api

import com.example.interviewstarter.data.remote.dto.ArticleDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticleApiService {

    @GET("articles")
    suspend fun getArticles(
        @Query("per_page") perPage: Int = 30,
        @Query("top") top: Int? = null,
        @Query("tag") tag: String? = null
    ): List<ArticleDto>

    companion object {
        fun getMockArticles(): List<ArticleDto> {
            return listOf(
                ArticleDto(
                    id = 1000000001L,
                    title = "Android Views & XML: Clean Architecture & Modern StateFlow Integration",
                    description = "Traditional Android Views combined with ViewBinding, Kotlin Coroutines, and ListAdapter deliver robust, predictable, and enterprise-ready UI layers.",
                    bodyMarkdown = "While modern declarative frameworks continue to evolve, the classic Android Views framework remains a rock-solid foundation in thousands of high-traffic production apps. Leveraging ViewBinding and ListAdapter provides exceptional list rendering performance and seamless backwards compatibility.",
                    url = "https://developer.android.com/topic/libraries/view-binding",
                    coverImage = "https://picsum.photos/seed/xml1/800/450",
                    socialImage = "https://picsum.photos/seed/xml1/800/450",
                    user = ArticleDto.UserDto(name = "Android Engineering"),
                    tagList = listOf("android", "xml", "kotlin", "mobile"),
                    publishedAt = "2026-08-12T10:00:00Z",
                    readingTimeMinutes = 5
                ),
                ArticleDto(
                    id = 1000000002L,
                    title = "Mastering Offline-First Architecture with Room 2.8 & Kotlin Coroutines",
                    description = "Offline-first architecture is vital for robust modern mobile applications. Learn how to combine Room reactive DAOs with background synchronization workers.",
                    bodyMarkdown = "Building responsive Android applications means treating local SQLite as the single source of truth. By combining Room Flow emissions with WorkManager and Retrofit, your UI stays instantly responsive even in airplane mode.",
                    url = "https://developer.android.com/training/data-storage/room",
                    coverImage = "https://picsum.photos/seed/room2/800/450",
                    socialImage = "https://picsum.photos/seed/room2/800/450",
                    user = ArticleDto.UserDto(name = "Kishor Ramani"),
                    tagList = listOf("android", "room", "sqlite", "mobile"),
                    publishedAt = "2026-08-11T14:30:00Z",
                    readingTimeMinutes = 7
                ),
                ArticleDto(
                    id = 1000000003L,
                    title = "On-Device AI & Gemini Nano Integration on Android",
                    description = "On-device AI provides ultra-low latency inference while keeping user data private. Explore practical Kotlin integration patterns for generative summarization.",
                    bodyMarkdown = "With Android AICore and Gemini Nano, developers can run text summarization, smart reply, and proofreading directly on hardware NPUs without server roundtrips or API cost overhead.",
                    url = "https://ai.google.dev",
                    coverImage = "https://picsum.photos/seed/ai3/800/450",
                    socialImage = "https://picsum.photos/seed/ai3/800/450",
                    user = ArticleDto.UserDto(name = "Google AI Research"),
                    tagList = listOf("ai", "gemini", "android"),
                    publishedAt = "2026-08-10T09:15:00Z",
                    readingTimeMinutes = 6
                ),
                ArticleDto(
                    id = 1000000004L,
                    title = "Clean Architecture with Koin 4.2 & ViewModel Scoping",
                    description = "Dependency injection is essential for clean decoupled architecture. Koin provides lightweight DSL-based DI without boilerplate annotation processing.",
                    bodyMarkdown = "Explore how Koin streamlines constructor injection across Repository, UseCase, and Jetpack ViewModel boundaries. We cover test harnesses and factory scopes.",
                    url = "https://insert-koin.io",
                    coverImage = "https://picsum.photos/seed/koin4/800/450",
                    socialImage = "https://picsum.photos/seed/koin4/800/450",
                    user = ArticleDto.UserDto(name = "Koin Community"),
                    tagList = listOf("koin", "kotlin", "mobile"),
                    publishedAt = "2026-08-09T16:45:00Z",
                    readingTimeMinutes = 4
                ),
                ArticleDto(
                    id = 1000000005L,
                    title = "Modern Kotlin Coroutines & Flow: StateFlow vs SharedFlow Deep Dive",
                    description = "Understand concurrency, backpressure, exception handling, and hot vs cold stream behaviors for high-performance reactive applications.",
                    bodyMarkdown = "Kotlin Coroutines revolutionized asynchronous programming on the JVM. In this guide, we break down channel buffers, structured concurrency, and sharing coroutine scopes.",
                    url = "https://kotlinlang.org/docs/coroutines-overview.html",
                    coverImage = "https://picsum.photos/seed/flow5/800/450",
                    socialImage = "https://picsum.photos/seed/flow5/800/450",
                    user = ArticleDto.UserDto(name = "Kotlin Experts"),
                    tagList = listOf("web", "cloud", "kotlin"),
                    publishedAt = "2026-08-08T11:20:00Z",
                    readingTimeMinutes = 8
                ),
                ArticleDto(
                    id = 1000000006L,
                    title = "Microservices Architecture & Cloud Deployment with Ktor & Docker",
                    description = "Ktor is a high-performance, asynchronous web framework built from the ground up for Kotlin and coroutines.",
                    bodyMarkdown = "Learn how to build lightweight REST & WebSocket microservices using Ktor, compile to lightweight containers, and deploy to Kubernetes clusters.",
                    url = "https://ktor.io",
                    coverImage = "https://picsum.photos/seed/ktor6/800/450",
                    socialImage = "https://picsum.photos/seed/ktor6/800/450",
                    user = ArticleDto.UserDto(name = "Cloud Native Group"),
                    tagList = listOf("devops", "cloud", "docker"),
                    publishedAt = "2026-08-07T18:00:00Z",
                    readingTimeMinutes = 5
                )
            )
        }
    }
}
