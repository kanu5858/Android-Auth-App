package com.kanu.loginregister.domain.repository

import com.kanu.loginregister.domain.model.User
import com.kanu.loginregister.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Resource<User>
    suspend fun register(name: String, email: String, password: String): Resource<Unit>
    suspend fun logout(): Resource<Unit>
    fun getAuthenticatedUser(): Flow<User?>
}