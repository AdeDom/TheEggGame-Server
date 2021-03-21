package com.adedom.teg.models.report.four

data class ItemCollectionMode(
    val modeId: String? = null,
    val mode: String? = null,
    val itemQtyAll: Int? = null,
    val itemAll: Int? = null,
    val itemCollectionItems: List<ItemCollectionItem> = emptyList(),
)
