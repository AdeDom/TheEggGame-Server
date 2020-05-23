package com.adedom.teg.networks

import com.adedom.teg.models.Player

data class PlayerResponse(
    var player: Player? = null
) : BaseResponse()
