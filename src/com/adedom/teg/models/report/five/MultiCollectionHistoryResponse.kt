package com.adedom.teg.models.report.five

data class MultiCollectionHistoryResponse(
    var dateAll: Int? = null,
    var multiCollectionDates: List<MultiCollectionDate> = emptyList(),
)
