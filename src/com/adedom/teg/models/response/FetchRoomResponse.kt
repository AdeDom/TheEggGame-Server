package com.adedom.teg.models.response

data class FetchRoomResponse(
    val roomId: Int,
    val roomNo: String,
    val name: String,
    val people: Int,
    val status: String,
    val startTime: Long? = null,
    val endTime: Long? = null,
    val dateTime: String,
)
