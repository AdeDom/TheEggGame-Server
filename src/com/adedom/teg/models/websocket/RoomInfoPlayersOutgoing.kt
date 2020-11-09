package com.adedom.teg.models.websocket

import com.adedom.teg.models.response.PlayerInfo

data class RoomInfoPlayersOutgoing(
    var success: Boolean = false,
    var message: String? = null,
    var roomInfoPlayers: List<PlayerInfo> = emptyList(),
)
