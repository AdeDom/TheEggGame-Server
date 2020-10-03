package com.adedom.teg.models.request

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("/api/account/state/{state}")
data class StateRequest(val state: String? = null)
