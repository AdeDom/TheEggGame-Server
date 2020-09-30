package com.adedom.teg.controller.auth.model

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("/api/auth/sign-up")
data class SignUpRequest(
    val username: String? = null,
    val password: String? = null,
    val name: String? = null,
    val gender: String? = null,
    val birthdate: String? = null,
)
