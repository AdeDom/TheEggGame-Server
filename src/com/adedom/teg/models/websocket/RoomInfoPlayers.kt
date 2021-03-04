package com.adedom.teg.models.websocket

data class RoomInfoPlayers(
    val playerId: String,
    val username: String,
    val name: String,
    val image: String?,
    val level: Int,
    val state: String?,
    val gender: String,
    val birthDate: String,
    val roleRoomInfo: String,
    val statusRoomInfo: String,
    val teamRoomInfo: String,
)
