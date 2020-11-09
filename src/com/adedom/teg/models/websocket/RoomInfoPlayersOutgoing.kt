package com.adedom.teg.models.websocket

data class RoomInfoPlayersOutgoing(
    var success: Boolean = false,
    var message: String? = null,
    var roomInfoPlayers: List<RoomInfoPlayers> = emptyList(),
)
