package com.adedom.teg.http.models.response

import com.adedom.teg.refactor.PlayerInfo

data class PlayerResponse(
    var success: Boolean = false,
    var message: String? = null,
    var playerInfo: PlayerInfo? = null
)
