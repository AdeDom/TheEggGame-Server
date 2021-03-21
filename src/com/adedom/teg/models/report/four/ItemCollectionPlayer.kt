package com.adedom.teg.models.report.four

data class ItemCollectionPlayer(
    val playerId: String? = null,
    val name: String? = null,
    val modeAll: Int? = null,
    val itemCollectionModes: List<ItemCollectionMode> = emptyList(),
)
