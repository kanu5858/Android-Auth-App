package com.kanu.loginregister.data.remote.dto

data class AuthResponse(
    val id: String,
    val name: String,
    val email: String,
    val token: String
)