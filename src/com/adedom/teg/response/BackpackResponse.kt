package com.adedom.teg.response

import com.adedom.teg.data.BASE_RESPONSE_MESSAGE
import com.adedom.teg.models.Backpack

data class BackpackResponse(
    var success: Boolean = false,
    var message: String? = BASE_RESPONSE_MESSAGE,
    var backpack: Backpack? = null
)
