package com.adedom.teg.data.models

data class SignUpDb(
    val username: String,
    val password: String,
    val name: String,
    val gender: String,
    val birthDate: Long,
)
