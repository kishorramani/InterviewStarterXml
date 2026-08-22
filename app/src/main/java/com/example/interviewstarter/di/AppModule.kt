package com.example.interviewstarter.di

import android.content.Context
import com.example.interviewstarter.core.network.NetworkMonitor
import com.example.interviewstarter.core.network.createHttpClient
import com.example.interviewstarter.data.auth.*
import com.example.interviewstarter.data.local.AppDatabase
import com.example.interviewstarter.data.local.PreferencesStorage
import com.example.interviewstarter.data.local.createDatabase
import com.example.interviewstarter.data.remote.api.UserApi
import com.example.interviewstarter.data.repository.UserRepositoryImpl
import com.example.interviewstarter.domain.repository.UserRepository
import com.example.interviewstarter.domain.usecase.*
import com.example.interviewstarter.presentation.auth.AuthViewModel
import com.example.interviewstarter.presentation.users.UserViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { createHttpClient() }
    single<AppDatabase> { createDatabase(get()) }
    single { get<AppDatabase>().userDao() }
    single { PreferencesStorage(get()) }

    single<TokenStorage> { AndroidTokenStorage(get()) }
    single { FakeAuthApi() }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }

    single { NetworkMonitor(get<Context>()) }
    single { UserApi(get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }

    factory { GetUsersUseCase(get()) }
    factory { GetPagedUsersUseCase(get()) }
    factory { SearchUsersUseCase(get()) }
    factory { SearchUsersFlowUseCase(get()) }

    viewModel { UserViewModel(get(), get()) }
    viewModel { AuthViewModel(get()) }
}
