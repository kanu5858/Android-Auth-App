package com.kanu.loginregister.data.repository

import com.kanu.loginregister.data.local.TokenManager
import com.kanu.loginregister.data.local.UserDao
import com.kanu.loginregister.data.local.entity.toUserEntity
import com.kanu.loginregister.data.remote.AuthApi
import com.kanu.loginregister.data.remote.dto.LoginRequest
import com.kanu.loginregister.data.remote.dto.RegisterRequest
import com.kanu.loginregister.domain.model.User
import com.kanu.loginregister.domain.repository.AuthRepository
import com.kanu.loginregister.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val tokenManager: TokenManager,
    private val userDao: UserDao
) : AuthRepository {

    override suspend fun login(email: String, password: String): Resource<User> {
        return try {
            val response = api.login(LoginRequest(email, password))
            val user = User(
                id = response.id,
                name = response.name,
                email = response.email,
                token = response.token
            )
            tokenManager.saveToken(response.token)
            userDao.insertUser(user.toUserEntity())
            Resource.Success(user)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    override suspend fun register(name: String, email: String, password: String): Resource<Unit> {
        return try {
            api.register(RegisterRequest(name, email, password))
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    override suspend fun logout(): Resource<Unit> {
        tokenManager.deleteToken()
        userDao.deleteUser()
        return Resource.Success(Unit)
    }

    override fun getAuthenticatedUser(): Flow<User?> {
        return userDao.getUser().map { it?.toUser() }
    }
}