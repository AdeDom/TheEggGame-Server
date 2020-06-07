package com.adedom.teg.response

import com.adedom.teg.models.RoomInfo

data class RoomInfosResponse(
    var roomInfo: List<RoomInfo>? = null
) : BaseResponse()
