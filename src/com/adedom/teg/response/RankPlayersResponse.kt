package com.adedom.teg.response

import com.adedom.teg.data.BASE_RESPONSE_MESSAGE
import com.adedom.teg.models.Player

data class RankPlayersResponse(
    var success: Boolean = false,
    var message: String? = BASE_RESPONSE_MESSAGE,
    var rankPlayers: List<Player>? = null
)
