package com.adedom.teg.models.request

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("/api/auth/refresh-token")
data class RefreshTokenRequest(
    val refreshToken: String? = null,
)
