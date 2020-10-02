package com.adedom.teg.response

data class SignInResponse(
    var success: Boolean = false,
    var message: String? = null,
    var accessToken: String? = null,
    var refreshToken: String? = null,
)
