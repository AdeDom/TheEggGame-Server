package com.adedom.teg.models.report.three

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("/api/report/room-history/{filter}")
data class RoomHistoryRequest(
    val filter: String? = null,
    val begin: Long? = null,
    val end: Long? = null,
)
