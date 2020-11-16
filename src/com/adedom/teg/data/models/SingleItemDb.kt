package com.adedom.teg.data.models

data class SingleItemDb(
    val singleId: Int? = null,
    val itemTypeId: Int? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val playerId: String? = null,
    val status: String? = null,
    val dateTimeCreated: Long? = null,
    val dateTimeUpdated: Long? = null,
)
