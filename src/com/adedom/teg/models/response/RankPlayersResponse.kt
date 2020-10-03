package com.adedom.teg.models.response

import com.adedom.teg.models.models.PlayerInfo

data class RankPlayersResponse(
    var success: Boolean = false,
    var message: String? = null,
    var rankPlayers: List<PlayerInfo> = emptyList(),
)
