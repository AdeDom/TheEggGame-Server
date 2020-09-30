package com.adedom.teg.controller.account.model

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("/api/account/change-password")
data class ChangePasswordRequest(
    val oldPassword: String? = null,
    val newPassword: String? = null
)
