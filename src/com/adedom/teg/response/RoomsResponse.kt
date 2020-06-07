package com.adedom.teg.response

import com.adedom.teg.models.Room

data class RoomsResponse(
    var room: List<Room>? = null
) : BaseResponse()
