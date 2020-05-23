package com.adedom.teg.response

import com.adedom.teg.models.Room

data class RoomsResponse(
    val room: List<Room>? = null
) : BaseResponse()
