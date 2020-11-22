package com.adedom.teg.models.websocket

import com.adedom.teg.models.response.PlayerInfo

data class PlaygroundSinglePlayerOutgoing(
    var players: List<PlayerInfo> = emptyList(),
)
