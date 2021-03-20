package com.adedom.teg.models.report.three

data class RoomHistoryResponse(
    var roomAll: Int? = null,
    var roomHistories: List<RoomHistory> = emptyList(),
)
