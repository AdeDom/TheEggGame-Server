package com.adedom.teg.models.response

import com.adedom.teg.refactor.Room

data class RoomsResponse(
    var success: Boolean = false,
    var message: String? = null,
    var room: List<Room>? = null,
)
