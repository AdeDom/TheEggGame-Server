package com.adedom.teg.models.response

data class Token(
    var accessToken: String? = null,
    var refreshToken: String? = null,
)
