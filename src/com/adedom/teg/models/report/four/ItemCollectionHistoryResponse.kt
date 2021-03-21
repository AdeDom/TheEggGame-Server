package com.adedom.teg.models.report.four

data class ItemCollectionHistoryResponse(
    var itemAll: Int? = null,
    var playerAll: Int? = null,
    var itemCollectionPlayers: List<ItemCollectionPlayer> = emptyList(),
)
