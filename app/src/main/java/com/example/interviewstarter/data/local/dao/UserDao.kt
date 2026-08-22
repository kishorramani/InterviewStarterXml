package com.example.interviewstarter.data.local.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.example.interviewstarter.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY id")
    fun observeUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUser(id: Int): UserEntity?

    @Query("""
        SELECT * FROM users
        WHERE name LIKE '%' || :query || '%'
           OR email LIKE '%' || :query || '%'
        ORDER BY id
    """)
    fun searchUsers(query: String): Flow<List<UserEntity>>

    @Query("SELECT * FROM users ORDER BY id")
    fun pagingSource(): PagingSource<Int, UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Delete
    suspend fun delete(user: UserEntity)

    @Query("DELETE FROM users")
    suspend fun deleteAll()
}
