package com.adedom.teg.models.report

data class RoomInfoResponse(
    var success: Boolean = false,
    var message: String? = null,
    var roomInfoList: List<RoomInfo> = emptyList(),
)
