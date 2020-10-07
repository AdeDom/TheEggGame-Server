package com.adedom.teg.models.response

import com.adedom.teg.data.models.RoomInfoDb

data class RoomInfoResponse(
    var success: Boolean = false,
    var message: String? = null,
    var roomInfo: List<RoomInfoDb>? = null,
)
