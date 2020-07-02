package com.adedom.teg.response

import com.adedom.teg.data.BASE_RESPONSE_MESSAGE
import com.adedom.teg.models.Player

data class RankResponse(
    var success: Boolean = false,
    var message: String? = BASE_RESPONSE_MESSAGE,
    var players: List<Player>? = null
)
