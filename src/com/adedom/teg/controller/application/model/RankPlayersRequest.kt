package com.adedom.teg.controller.application.model

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("/api/application/rank/{rank}")
data class RankPlayersRequest(
    val rank: String? = null,
    val search: String? = null,
    val limit: String? = null
)
