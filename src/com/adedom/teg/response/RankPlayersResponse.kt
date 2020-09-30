package com.adedom.teg.response

import com.adedom.teg.models.PlayerInfo

data class RankPlayersResponse(
    var success: Boolean = false,
    var message: String? = null,
    var rankPlayers: List<PlayerInfo> = emptyList(),
)
