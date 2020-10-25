package com.adedom.teg.models.response

data class MissionResponse(
    var success: Boolean = false,
    var message: String? = null,
    var missionInfo: MissionInfo? = null,
)
