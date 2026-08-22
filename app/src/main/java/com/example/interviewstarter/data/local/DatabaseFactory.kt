package com.example.interviewstarter.data.local

import android.content.Context
import androidx.room.Room

fun createDatabase(context: Context): AppDatabase =
    Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "interview_starter.db"
    ).build()
