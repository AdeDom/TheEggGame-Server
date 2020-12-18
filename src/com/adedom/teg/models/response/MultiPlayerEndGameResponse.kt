package com.adedom.teg.models.response

data class MultiPlayerEndGameResponse(
    var success: Boolean = false,
    var message: String? = null,
    var endGame: MultiPlayerEndGame? = null,
)
