package com.adedom.teg.models.websocket

data class RoomInfoTegMultiOutgoing(
    var success: Boolean = false,
    var message: String? = null,
    var roomNo: String? = null,
)
