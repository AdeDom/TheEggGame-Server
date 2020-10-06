package com.adedom.teg.models.models

data class PlayerInfoDb(
    val playerId: String? = null,
    val username: String? = null,
    val name: String? = null,
    val image: String? = null,
    val level: Int? = null,
    val state: String? = null,
    val gender: String? = null,
    val birthdate: Long? = null,
)
