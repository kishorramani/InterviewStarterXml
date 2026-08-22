package com.example.interviewstarter.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.interviewstarter.domain.usecase.GetUsersUseCase
import com.example.interviewstarter.core.network.NetworkResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserSyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams), KoinComponent {

    private val getUsers: GetUsersUseCase by inject()

    override suspend fun doWork(): Result =
        when (getUsers()) {
            is NetworkResult.Success -> Result.success()
            is NetworkResult.HttpError -> {
                if (runAttemptCount < 3) Result.retry()
                else Result.failure()
            }
            is NetworkResult.NetworkError -> Result.retry()
            is NetworkResult.UnknownError -> Result.failure()
        }
}
