package com.example.interviewstarter.core.network

sealed interface NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>

    data class HttpError(
        val code: Int,
        val message: String
    ) : NetworkResult<Nothing>

    data class NetworkError(
        val message: String,
        val cause: Throwable? = null
    ) : NetworkResult<Nothing>

    data class UnknownError(
        val message: String,
        val cause: Throwable? = null
    ) : NetworkResult<Nothing>
}
