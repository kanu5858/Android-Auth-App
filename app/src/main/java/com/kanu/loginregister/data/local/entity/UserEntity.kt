package com.kanu.loginregister.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kanu.loginregister.domain.model.User

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val token: String?
) {
    fun toUser(): User = User(id, name, email, token)
}

fun User.toUserEntity(): UserEntity = UserEntity(id, name, email, token)