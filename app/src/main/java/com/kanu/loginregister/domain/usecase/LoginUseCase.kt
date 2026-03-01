package com.kanu.loginregister.domain.usecase

import com.kanu.loginregister.domain.model.User
import com.kanu.loginregister.domain.repository.AuthRepository
import com.kanu.loginregister.util.Resource
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Resource<User> {
        if (email.isBlank() || password.isBlank()) {
            return Resource.Error("Email and password cannot be empty")
        }
        return repository.login(email, password)
    }
}