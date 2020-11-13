package com.adedom.teg.models.request

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("/api/application/change-current-mode/{mode}")
data class ChangeCurrentModeRequest(
    val mode: String? = null,
)
