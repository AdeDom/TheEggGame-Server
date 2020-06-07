package com.adedom.teg.request

data class SignUpRequest(
    val username: String? = null,
    val password: String? = null,
    val name: String? = null,
    val gender: String? = null
)
