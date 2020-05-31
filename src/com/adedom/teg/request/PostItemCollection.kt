package com.adedom.teg.request

data class PostItemCollection(
    val playerId: Int? = null,
    val itemId: Int? = null,
    val qty: Int? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)