package com.example.interviewstarter.data.repository

import android.content.Context
import android.os.Build
import com.example.interviewstarter.data.local.dao.ArticleDao
import com.example.interviewstarter.domain.model.PlatformInfo
import com.example.interviewstarter.domain.repository.PlatformRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlatformRepositoryImpl(
    private val context: Context,
    private val articleDao: ArticleDao
) : PlatformRepository {

    override fun getPlatformInfo(): PlatformInfo {
        val runtime = Runtime.getRuntime()
        val totalMemoryMb = runtime.totalMemory() / (1024 * 1024)
        val freeMemoryMb = runtime.freeMemory() / (1024 * 1024)
        val maxMemoryMb = runtime.maxMemory() / (1024 * 1024)
        val usedMemoryMb = totalMemoryMb - freeMemoryMb

        val isSimulator = Build.FINGERPRINT.startsWith("generic") ||
                Build.FINGERPRINT.startsWith("unknown") ||
                Build.MODEL.contains("google_sdk") ||
                Build.MODEL.contains("Emulator") ||
                Build.MODEL.contains("Android SDK built for x86") ||
                Build.MANUFACTURER.contains("Genymotion") ||
                Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic") ||
                "google_sdk" == Build.PRODUCT

        val cpuArch = Build.SUPPORTED_ABIS.firstOrNull() ?: "Unknown"

        return PlatformInfo(
            osName = "Android",
            osVersion = "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})",
            deviceModel = "${Build.MANUFACTURER} ${Build.MODEL}",
            cpuArchitecture = cpuArch,
            memoryInfo = "Used: ${usedMemoryMb}MB / Max: ${maxMemoryMb}MB (Free: ${freeMemoryMb}MB)",
            isSimulator = isSimulator,
            kotlinVersion = "2.3.20",
            composeVersion = "N/A (Android XML Views)"
        )
    }

    override suspend fun clearCache(): Unit = withContext(Dispatchers.IO) {
        articleDao.clearNonBookmarkedArticles()
    }
}
