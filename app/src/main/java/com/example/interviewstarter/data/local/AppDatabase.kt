package com.example.interviewstarter.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.interviewstarter.data.local.dao.UserDao
import com.example.interviewstarter.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
