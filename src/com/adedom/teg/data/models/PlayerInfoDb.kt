package com.adedom.teg.data.models

data class PlayerInfoDb(
    val playerId: String? = null,
    val username: String? = null,
    val name: String? = null,
    val image: String? = null,
    val level: Int? = null,
    val state: String? = null,
    val gender: String? = null,
    val birthDate: Long? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
)
