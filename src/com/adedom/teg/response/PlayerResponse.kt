package com.adedom.teg.response

import com.adedom.teg.models.Player

data class PlayerResponse(
    var player: Player? = null
) : BaseResponse()
