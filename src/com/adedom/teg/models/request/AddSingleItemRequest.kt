package com.adedom.teg.models.request

data class AddSingleItemRequest(
    val itemId: Int? = null,
    val qty: Int? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
)
