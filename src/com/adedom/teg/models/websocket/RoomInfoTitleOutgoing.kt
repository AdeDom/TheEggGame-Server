package com.adedom.teg.models.websocket

import com.adedom.teg.models.response.FetchRoomResponse

data class RoomInfoTitleOutgoing(
    var success: Boolean = false,
    var message: String? = null,
    var roomInfoTitle: FetchRoomResponse? = null,
    var roomNo: String? = null,
)
