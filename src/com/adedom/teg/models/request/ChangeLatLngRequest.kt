package com.adedom.teg.models.request

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("/api/account/change-lat-lng")
data class ChangeLatLngRequest(
    val latitude: Double? = null,
    val longitude: Double? = null,
)
