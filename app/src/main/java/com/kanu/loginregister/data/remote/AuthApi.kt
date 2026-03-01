package com.kanu.loginregister.data.remote

import com.kanu.loginregister.data.remote.dto.LoginRequest
import com.kanu.loginregister.data.remote.dto.AuthResponse
import com.kanu.loginregister.data.remote.dto.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("register")
    suspend fun register(@Body request: RegisterRequest)

    companion object {
        const val BASE_URL = "https://mock-api.com/"
    }
}