package com.adedom.teg.request.auth

import io.ktor.locations.Location

@Location("/api/auth/sign-in")
data class SignInRequest(
    val username: String? = null,
    val password: String? = null
)
