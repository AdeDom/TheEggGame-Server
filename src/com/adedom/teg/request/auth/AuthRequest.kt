package com.adedom.teg.request.auth

import io.ktor.locations.Location

@Location("/api/auth/sign-in")
data class SignInRequest(
    val username: String? = null,
    val password: String? = null
)

@Location("/api/auth/sign-up")
data class SignUpRequest(
    val username: String? = null,
    val password: String? = null,
    val name: String? = null,
    val gender: String? = null
)
