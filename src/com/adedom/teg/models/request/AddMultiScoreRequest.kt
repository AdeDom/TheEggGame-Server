package com.adedom.teg.models.request

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("/api/multi/add-multi-score")
data class AddMultiScoreRequest(
    val multiId: Int? = null,
)
