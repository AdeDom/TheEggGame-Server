package com.adedom.teg.models.report

data class MultiCollectionResponse(
    var success: Boolean = false,
    var message: String? = null,
    var multiCollections: List<MultiCollection> = emptyList(),
)
