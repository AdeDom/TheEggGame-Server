package com.adedom.teg.response

import com.adedom.teg.models.Player

data class RankResponse(
    var players: List<Player>? = null
) : BaseResponse()
