package com.adedom.teg.response

import com.adedom.teg.models.Backpack

data class BackpackResponse(
    var backpack: Backpack? = null
) : BaseResponse()
