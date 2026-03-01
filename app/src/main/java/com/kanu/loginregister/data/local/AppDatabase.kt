package com.kanu.loginregister.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kanu.loginregister.data.local.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val userDao: UserDao

    companion object {
        const val DATABASE_NAME = "auth_db"
    }
}