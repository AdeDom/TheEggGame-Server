package com.adedom.teg.request

data class MultiCollectionRequest(
    val multiId: Int? = null,
    val roomNo: String? = null,
    val playerId: Int? = null,
    val team: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)