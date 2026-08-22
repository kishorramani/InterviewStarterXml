package com.example.interviewstarter.domain.usecase

import com.example.interviewstarter.domain.model.PlatformInfo
import com.example.interviewstarter.domain.repository.PlatformRepository

class GetPlatformMetricsUseCase(
    private val repository: PlatformRepository
) {
    operator fun invoke(): PlatformInfo {
        return repository.getPlatformInfo()
    }

    suspend fun clearCache() {
        repository.clearCache()
    }
}
