package com.adedom.teg.models.response

data class RoomsResponse(
    var success: Boolean = false,
    var message: String? = null,
    var rooms: List<FetchRoomResponse> = emptyList(),
)
