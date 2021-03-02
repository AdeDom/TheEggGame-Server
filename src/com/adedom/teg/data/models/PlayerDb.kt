package com.adedom.teg.data.models

data class PlayerDb(
    val playerId: String,
    val username: String,
    val password: String,
    val name: String,
    val image: String?,
    val gender: String,
    val birthDate: Long,
    val state: String?,
    val latitude: Double?,
    val longitude: Double?,
    val currentMode: String?,
    val dateTimeCreated: Long,
    val dateTimeUpdated: Long?,
)
