package com.adedom.teg.models.request

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("/api/account/change-profile")
data class ChangeProfileRequest(
    val name: String? = null,
    val gender: String? = null,
    val birthDate: String? = null,
)
