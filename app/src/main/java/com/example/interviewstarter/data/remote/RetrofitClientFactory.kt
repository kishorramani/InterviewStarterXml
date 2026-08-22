package com.example.interviewstarter.data.remote

import com.example.interviewstarter.data.remote.api.ArticleApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClientFactory {

    private const val BASE_URL = "https://dev.to/api/"

    fun createMoshi(): Moshi {
        return Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    fun createArticleApiService(
        okHttpClient: OkHttpClient = createOkHttpClient(),
        moshi: Moshi = createMoshi()
    ): ArticleApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        return retrofit.create(ArticleApiService::class.java)
    }
}
