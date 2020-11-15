package com.adedom.teg.models.websocket

import com.adedom.teg.data.models.SingleItemDb

data class SingleItemOutgoing(
    val singleItems: List<SingleItemDb> = emptyList(),
)
