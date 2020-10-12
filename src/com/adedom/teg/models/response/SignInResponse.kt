package com.adedom.teg.models.response

data class SignInResponse(
    var success: Boolean = false,
    var message: String? = null,
    var token: Token? = null,
)
