package com.adedom.teg.response

import com.adedom.teg.models.Backpack

data class BackpackResponse(
    var success: Boolean = false,
    var message: String? = null,
    var backpack: Backpack? = null,
)
