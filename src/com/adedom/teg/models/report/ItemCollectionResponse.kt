package com.adedom.teg.models.report

data class ItemCollectionResponse(
    var success: Boolean = false,
    var message: String? = null,
    var itemCollections: List<ItemCollection> = emptyList(),
)
