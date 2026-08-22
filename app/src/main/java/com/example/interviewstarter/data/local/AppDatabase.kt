package com.example.interviewstarter.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.interviewstarter.data.local.dao.ArticleDao
import com.example.interviewstarter.data.local.dao.UserDao
import com.example.interviewstarter.data.local.entity.ArticleEntity
import com.example.interviewstarter.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, ArticleEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun articleDao(): ArticleDao
}
