package com.adedom.teg.models.response

data class MultiPlayerEndGame(
    val scoreTeamA: Int,
    val scoreTeamB: Int,
    val resultTeamA: String? = null,
    val resultTeamB: String? = null,
    val isBonusEndGame: Boolean = false,
)
