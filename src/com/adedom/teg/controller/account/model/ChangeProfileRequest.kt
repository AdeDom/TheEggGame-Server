package com.adedom.teg.controller.account.model

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("/api/account/change-profile")
data class ChangeProfileRequest(
    val name: String? = null,
    val gender: String? = null,
    val birthdate: String? = null,
)
