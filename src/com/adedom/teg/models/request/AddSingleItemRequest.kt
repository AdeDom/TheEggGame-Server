package com.adedom.teg.models.request

data class AddSingleItemRequest(
    val itemTypeId: Int? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
)
