package com.example.interviewstarter.domain.repository

import com.example.interviewstarter.domain.model.PlatformInfo

interface PlatformRepository {
    fun getPlatformInfo(): PlatformInfo
    suspend fun clearCache()
}
