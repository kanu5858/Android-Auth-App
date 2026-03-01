package com.kanu.loginregister.data.remote.dto

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)