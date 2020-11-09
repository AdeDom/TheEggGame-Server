package com.adedom.teg.models.websocket

data class RoomInfoPlayers(
    val playerId: String? = null,
    val username: String? = null,
    val name: String? = null,
    val image: String? = null,
    val level: Int? = null,
    val state: String? = null,
    val gender: String? = null,
    val birthDate: String? = null,
    val roleRoomInfo: String? = null,
    val statusRoomInfo: String? = null,
    val teamRoomInfo: String? = null,
)
