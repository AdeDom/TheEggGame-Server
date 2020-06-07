package com.adedom.teg.response

import com.adedom.teg.models.RoomInfo

data class RoomInfoResponse(
    var roomInfo: List<RoomInfo>? = null
) : BaseResponse()
