package com.adedom.teg.models.request

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("/api/multi/change-team/{team}")
data class ChangeTeamRequest(
    val team: String? = null,
)
