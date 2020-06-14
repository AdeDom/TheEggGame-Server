package com.adedom.teg.request

data class PasswordRequest(
    val oldPassword: String? = null,
    val newPassword: String? = null
)
