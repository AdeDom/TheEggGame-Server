package com.adedom.teg.http.models.response

data class RoomResponse(
    var success: Boolean = false,
    var message: String? = null,
    var roomNo: String? = null,
)
