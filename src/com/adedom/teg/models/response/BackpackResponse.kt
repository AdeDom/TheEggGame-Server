package com.adedom.teg.models.response

import com.adedom.teg.models.models.Backpack

data class BackpackResponse(
    var success: Boolean = false,
    var message: String? = null,
    var backpack: Backpack? = null,
)
