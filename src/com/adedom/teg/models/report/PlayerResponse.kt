package com.adedom.teg.models.report

data class PlayerResponse(
    var success: Boolean = false,
    var message: String? = null,
    var players: List<Player> = emptyList(),
)
