package com.adedom.teg.models.response

import com.adedom.teg.models.websocket.RoomInfoPlayers

data class FetchMultiPlayerResponse(
    var success: Boolean = false,
    var message: String? = null,
    var roomInfoTitle: FetchRoomResponse? = null,
    var roomInfoPlayers: List<RoomInfoPlayers> = emptyList(),
)
