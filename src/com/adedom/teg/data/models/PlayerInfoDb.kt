package com.adedom.teg.data.models

data class PlayerInfoDb(
    val playerId: String,
    val username: String,
    val name: String,
    val image: String?,
    val level: Int?,
    val state: String?,
    val gender: String,
    val birthDate: Long,
    val latitude: Double?,
    val longitude: Double?,
)
