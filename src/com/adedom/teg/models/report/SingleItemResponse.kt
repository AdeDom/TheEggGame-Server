package com.adedom.teg.models.report

data class SingleItemResponse(
    var success: Boolean = false,
    var message: String? = null,
    var singleItems: List<SingleItem> = emptyList(),
)
