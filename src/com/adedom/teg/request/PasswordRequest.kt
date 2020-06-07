package com.adedom.teg.request

data class PasswordRequest(
    val playerId: Int? = null,
    val oldPassword: String? = null,
    val newPassword: String? = null
)
