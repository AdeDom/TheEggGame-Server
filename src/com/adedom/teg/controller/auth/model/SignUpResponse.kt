package com.adedom.teg.controller.auth.model

data class SignUpResponse(
    var success: Boolean = false,
    var message: String? = null,
    var accessToken: String? = null
)
