package com.adedom.teg.request.account

import io.ktor.locations.*

@Location("/api/account/image-profile")
class ImageProfile

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
