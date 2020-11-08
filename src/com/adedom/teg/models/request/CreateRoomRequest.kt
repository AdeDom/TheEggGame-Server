package com.adedom.teg.models.request

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("/api/multi/create-room")
data class CreateRoomRequest(
    val roomName: String? = null,
    val roomPeople: Int? = null,
)
