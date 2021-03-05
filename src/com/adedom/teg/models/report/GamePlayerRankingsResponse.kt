package com.adedom.teg.models.report

data class GamePlayerRankingsResponse(
    var success: Boolean = false,
    var message: String? = null,
    var gamePlayerRankings: List<GamePlayerRanking> = emptyList(),
)
