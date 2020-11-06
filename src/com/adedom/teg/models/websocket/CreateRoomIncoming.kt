package com.adedom.teg.models.websocket

data class CreateRoomIncoming(
    val roomName: String? = null,
    val roomPeople: Int? = null,
)
