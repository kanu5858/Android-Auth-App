package com.kanu.loginregister.presentation.login

data class LoginState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val isPasswordVisible: Boolean = false
)