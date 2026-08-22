package com.example.interviewstarter.domain.model

enum class Category(val displayName: String, val tag: String) {
    ALL("All Topics", "all"),
    MOBILE("Mobile & Android", "mobile"),
    AI_ML("AI & ML", "ai"),
    WEB("Web & Cloud", "web"),
    DEVOPS("Architecture & DevOps", "devops");

    companion object {
        fun fromTag(tag: String): Category {
            return entries.firstOrNull { it.tag.equals(tag, ignoreCase = true) } ?: ALL
        }
    }
}
