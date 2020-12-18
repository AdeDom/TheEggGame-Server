package com.adedom.teg.models.response

data class MultiPlayerEndGame(
    val scoreTeamA: Int? = null,
    val scoreTeamB: Int? = null,
    val resultTeamA: String? = null,
    val resultTeamB: String? = null,
    val isBonusEndGame: Boolean = false,
)
