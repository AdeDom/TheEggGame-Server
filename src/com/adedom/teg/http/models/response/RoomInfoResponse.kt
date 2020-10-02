package com.adedom.teg.http.models.response

import com.adedom.teg.refactor.RoomInfo

data class RoomInfoResponse(
    var success: Boolean = false,
    var message: String? = null,
    var roomInfo: List<RoomInfo>? = null,
)
