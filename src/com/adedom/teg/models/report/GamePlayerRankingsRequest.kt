package com.adedom.teg.models.report

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("/api/report/game-player-rankings/{level}")
data class GamePlayerRankingsRequest(
    val level: String? = null,
    val begin: Int? = null,
    val end: Int? = null,
)
