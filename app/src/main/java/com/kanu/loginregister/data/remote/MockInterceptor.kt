package com.kanu.loginregister.data.remote

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class MockInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val path = chain.request().url.encodedPath
        
        val responseString = when {
            path.endsWith("login") -> {
                "{\"id\":\"1\", \"name\":\"Kanu\", \"email\":\"kanu@example.com\", \"token\":\"mock_token_123\"}"
            }
            path.endsWith("register") -> {
                "{}"
            }
            else -> ""
        }

        return Response.Builder()
            .code(200)
            .message("OK")
            .request(chain.request())
            .protocol(Protocol.HTTP_1_1)
            .body(responseString.toResponseBody("application/json".toMediaTypeOrNull()))
            .addHeader("content-type", "application/json")
            .build()
    }
}