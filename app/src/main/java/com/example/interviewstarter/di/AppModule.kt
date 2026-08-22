package com.example.interviewstarter.di

import android.content.Context
import com.example.interviewstarter.core.network.NetworkMonitor
import com.example.interviewstarter.core.network.createHttpClient
import com.example.interviewstarter.data.auth.AndroidTokenStorage
import com.example.interviewstarter.data.auth.AuthRepository
import com.example.interviewstarter.data.auth.AuthRepositoryImpl
import com.example.interviewstarter.data.auth.FakeAuthApi
import com.example.interviewstarter.data.auth.TokenStorage
import com.example.interviewstarter.data.local.AppDatabase
import com.example.interviewstarter.data.local.PreferencesStorage
import com.example.interviewstarter.data.local.createDatabase
import com.example.interviewstarter.data.remote.RetrofitClientFactory
import com.example.interviewstarter.data.remote.api.ArticleApiService
import com.example.interviewstarter.data.remote.api.UserApi
import com.example.interviewstarter.data.repository.ArticleRepositoryImpl
import com.example.interviewstarter.data.repository.PlatformRepositoryImpl
import com.example.interviewstarter.data.repository.UserRepositoryImpl
import com.example.interviewstarter.domain.repository.ArticleRepository
import com.example.interviewstarter.domain.repository.PlatformRepository
import com.example.interviewstarter.domain.repository.UserRepository
import com.example.interviewstarter.domain.usecase.GetArticlesUseCase
import com.example.interviewstarter.domain.usecase.GetBookmarkedArticlesUseCase
import com.example.interviewstarter.domain.usecase.GetPagedUsersUseCase
import com.example.interviewstarter.domain.usecase.GetPlatformMetricsUseCase
import com.example.interviewstarter.domain.usecase.GetUsersUseCase
import com.example.interviewstarter.domain.usecase.SearchArticlesUseCase
import com.example.interviewstarter.domain.usecase.SearchUsersFlowUseCase
import com.example.interviewstarter.domain.usecase.SearchUsersUseCase
import com.example.interviewstarter.domain.usecase.ToggleBookmarkUseCase
import com.example.interviewstarter.presentation.auth.AuthViewModel
import com.example.interviewstarter.presentation.bookmarks.BookmarksViewModel
import com.example.interviewstarter.presentation.detail.DetailViewModel
import com.example.interviewstarter.presentation.feed.FeedViewModel
import com.example.interviewstarter.presentation.settings.SettingsViewModel
import com.example.interviewstarter.presentation.users.UserViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Network & Serialization
    single { createHttpClient() }
    single { RetrofitClientFactory.createOkHttpClient() }
    single { RetrofitClientFactory.createMoshi() }
    single<ArticleApiService> { RetrofitClientFactory.createArticleApiService(get(), get()) }
    single { NetworkMonitor(get<Context>()) }

    // Local Storage & Database
    single<AppDatabase> { createDatabase(get()) }
    single { get<AppDatabase>().userDao() }
    single { get<AppDatabase>().articleDao() }
    single { PreferencesStorage(get()) }

    // Auth
    single<TokenStorage> { AndroidTokenStorage(get()) }
    single { FakeAuthApi() }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }

    // Repositories
    single { UserApi(get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<ArticleRepository> { ArticleRepositoryImpl(get(), get()) }
    single<PlatformRepository> { PlatformRepositoryImpl(get(), get()) }

    // UseCases
    factory { GetUsersUseCase(get()) }
    factory { GetPagedUsersUseCase(get()) }
    factory { SearchUsersUseCase(get()) }
    factory { SearchUsersFlowUseCase(get()) }

    factory { GetArticlesUseCase(get()) }
    factory { SearchArticlesUseCase(get()) }
    factory { ToggleBookmarkUseCase(get()) }
    factory { GetBookmarkedArticlesUseCase(get()) }
    factory { GetPlatformMetricsUseCase(get()) }

    // ViewModels
    viewModel { UserViewModel(get(), get()) }
    viewModel { AuthViewModel(get()) }
    viewModel { FeedViewModel(get(), get(), get()) }
    viewModel { DetailViewModel(get(), get()) }
    viewModel { BookmarksViewModel(get(), get()) }
    viewModel { SettingsViewModel(get()) }
}
