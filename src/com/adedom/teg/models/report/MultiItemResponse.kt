package com.adedom.teg.models.report

data class MultiItemResponse(
    var success: Boolean = false,
    var message: String? = null,
    var multiItems: List<MultiItem> = emptyList(),
)
