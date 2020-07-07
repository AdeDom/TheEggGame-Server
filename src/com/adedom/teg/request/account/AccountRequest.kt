package com.adedom.teg.request.account

import io.ktor.locations.Location

@Location("/api/account/image-profile")
class ImageProfile

@Location("/api/account/player-info")
class PlayerInfoRequest

@Location("/api/account/state/{state}")
data class StateRequest(val state: String? = null)

@Location("/api/account/change-password")
data class ChangePasswordRequest(
    val oldPassword: String? = null,
    val newPassword: String? = null
)

@Location("/api/account/change-profile")
data class ChangeProfileRequest(
    val name: String? = null,
    val gender: String? = null
)
