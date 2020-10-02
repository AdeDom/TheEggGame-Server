package com.adedom.teg.http.models.response

import com.adedom.teg.refactor.PlayerInfo

data class RankPlayersResponse(
    var success: Boolean = false,
    var message: String? = null,
    var rankPlayers: List<PlayerInfo> = emptyList(),
)
