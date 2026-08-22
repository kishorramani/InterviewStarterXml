package com.example.interviewstarter.work

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object WorkScheduler {
    private const val USER_SYNC = "user_sync"

    fun scheduleUserSync(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = PeriodicWorkRequestBuilder<UserSyncWorker>(
            15,
            TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            USER_SYNC,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    fun cancelUserSync(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(USER_SYNC)
    }
}
