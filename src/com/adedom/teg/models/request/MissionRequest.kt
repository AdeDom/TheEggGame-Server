package com.adedom.teg.models.request

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("/api/application/mission")
data class MissionRequest(
    val mode: String? = null,
)
