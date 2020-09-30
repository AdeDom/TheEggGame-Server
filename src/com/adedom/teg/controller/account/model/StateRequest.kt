package com.adedom.teg.controller.account.model

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("/api/account/state/{state}")
data class StateRequest(val state: String? = null)
