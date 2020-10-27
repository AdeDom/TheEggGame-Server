package com.adedom.teg.models.response

data class FetchRoomResponse(
    val roomId: Int? = null,
    val roomNo: String? = null,
    val name: String? = null,
    val people: Int? = null,
    val status: String? = null,
    val dateTime: String? = null,
)
