package com.adedom.teg.models.request

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("/api/multi/join-room-info")
data class JoinRoomInfoRequest(
    val roomNo: String? = null,
)
