package com.adedom.teg.models.response

data class PlayerInfo(
    val playerId: String,
    val username: String,
    val name: String,
    val image: String?,
    val level: Int,
    val state: String?,
    val gender: String,
    val birthDate: String,
    val latitude: Double?,
    val longitude: Double?,
)
