package com.adedom.teg.controller.auth.model

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("/api/auth/refresh-token")
data class RefreshTokenRequest(
    val refreshToken: String? = null,
)
