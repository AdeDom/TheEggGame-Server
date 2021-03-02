package com.adedom.teg.models.report

data class RoomResponse(
    var success: Boolean = false,
    var message: String? = null,
    var rooms: List<Room> = emptyList(),
)
