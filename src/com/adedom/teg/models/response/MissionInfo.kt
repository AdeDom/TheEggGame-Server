package com.adedom.teg.models.response

data class MissionInfo(
    val isDelivery: Boolean = false,
    val isDeliveryCompleted: Boolean = false,
    val isSingle: Boolean = false,
    val isSingleCompleted: Boolean = false,
    val isMulti: Boolean = false,
    val isMultiCompleted: Boolean = false,
)
