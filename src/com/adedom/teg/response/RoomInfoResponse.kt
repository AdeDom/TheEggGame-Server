package com.adedom.teg.response

import com.adedom.teg.data.BASE_RESPONSE_MESSAGE
import com.adedom.teg.models.RoomInfo

data class RoomInfoResponse(
    var success: Boolean = false,
    var message: String? = BASE_RESPONSE_MESSAGE,
    var roomInfo: List<RoomInfo>? = null
)
