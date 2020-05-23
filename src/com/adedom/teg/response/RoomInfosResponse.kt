package com.adedom.teg.response

import com.adedom.teg.models.RoomInfo

data class RoomInfosResponse(
    val roomInfo: List<RoomInfo>? = null
) : BaseResponse()
