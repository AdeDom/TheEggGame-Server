package com.adedom.teg.response

import com.adedom.teg.data.BASE_RESPONSE_MESSAGE
import com.adedom.teg.models.PlayerInfo

data class RankPlayersResponse(
    var success: Boolean = false,
    var message: String? = BASE_RESPONSE_MESSAGE,
    var rankPlayers: List<PlayerInfo>? = null
)
