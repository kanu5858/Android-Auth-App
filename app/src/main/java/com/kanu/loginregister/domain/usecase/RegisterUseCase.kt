package com.kanu.loginregister.domain.usecase

import com.kanu.loginregister.domain.repository.AuthRepository
import com.kanu.loginregister.util.Resource
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(name: String, email: String, password: String, confirmPass: String): Resource<Unit> {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            return Resource.Error("Fields cannot be empty")
        }
        if (password != confirmPass) {
            return Resource.Error("Passwords do not match")
        }
        if (password.length < 6) {
            return Resource.Error("Password must be at least 6 characters")
        }
        return repository.register(name, email, password)
    }
}