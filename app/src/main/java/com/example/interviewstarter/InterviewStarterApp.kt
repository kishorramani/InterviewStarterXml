package com.example.interviewstarter

import android.app.Application
import com.example.interviewstarter.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class InterviewStarterApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@InterviewStarterApp)
            modules(appModule)
        }
    }
}
