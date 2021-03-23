package com.adedom.teg.models.report.six

data class SingleItemHistoryResponse(
    var dateAll: Int? = null,
    var singleItemDates: List<SingleItemDate> = emptyList(),
)
