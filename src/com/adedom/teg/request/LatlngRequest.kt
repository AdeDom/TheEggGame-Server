package com.adedom.teg.request

data class LatlngRequest(
    val roomNo: String? = null,
    val playerId: Int? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)