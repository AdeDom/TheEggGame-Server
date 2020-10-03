package com.adedom.teg.models.request

data class MultiCollectionRequest(
    val multiId: Int? = null,
    val roomNo: String? = null,
    val playerId: String? = null,
    val team: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)
