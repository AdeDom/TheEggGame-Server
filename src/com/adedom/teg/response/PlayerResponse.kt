package com.adedom.teg.response

import com.adedom.teg.models.PlayerInfo

data class PlayerResponse(
    var success: Boolean = false,
    var message: String? = null,
    var playerInfo: PlayerInfo? = null
)
