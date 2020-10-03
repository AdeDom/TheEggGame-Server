package com.adedom.teg.models.response

import com.adedom.teg.models.models.PlayerInfo

data class PlayerInfoResponse(
    var success: Boolean = false,
    var message: String? = null,
    var playerInfo: PlayerInfo? = null,
)
