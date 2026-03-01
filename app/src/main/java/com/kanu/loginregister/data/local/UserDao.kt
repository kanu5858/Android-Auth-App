package com.kanu.loginregister.data.local

import androidx.room.*
import com.kanu.loginregister.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM user_table LIMIT 1")
    fun getUser(): Flow<UserEntity?>

    @Query("DELETE FROM user_table")
    suspend fun deleteUser()
}