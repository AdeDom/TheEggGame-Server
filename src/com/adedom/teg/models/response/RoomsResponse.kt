package com.adedom.teg.models.response

import com.adedom.teg.data.models.RoomDb

data class RoomsResponse(
    var success: Boolean = false,
    var message: String? = null,
    var room: List<RoomDb>? = null,
)
