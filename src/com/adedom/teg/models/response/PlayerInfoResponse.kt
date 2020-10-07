package com.adedom.teg.models.response

data class PlayerInfoResponse(
    var success: Boolean = false,
    var message: String? = null,
    var playerInfo: PlayerInfo? = null,
)
