package com.adedom.teg.models.response

data class RankPlayersResponse(
    var success: Boolean = false,
    var message: String? = null,
    var rankPlayers: List<PlayerInfo> = emptyList(),
)
