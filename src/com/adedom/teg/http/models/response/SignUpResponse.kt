package com.adedom.teg.http.models.response

data class SignUpResponse(
    var success: Boolean = false,
    var message: String? = null,
    var accessToken: String? = null,
    var refreshToken: String? = null,
)
