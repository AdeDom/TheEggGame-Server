package com.adedom.teg.models.report

data class GamePlayerRankingsResponse(
    var success: Boolean = false,
    var message: String? = null,
    var peopleAll: Int? = null,
    var genderCount: GenderCount? = null,
    var stateCount: StateCount? = null,
    var modeCount: ModeCount? = null,
    var gamePlayerRankings: List<GamePlayerRanking> = emptyList(),
)
