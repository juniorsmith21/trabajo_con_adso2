package com.example.colaboracionadso2

data class LoginResponse(
    val id: Int,
    val username: String,
    val email: String,
    val firstName: String,
    val accessToken: String,
    val refreshToken: String
)
