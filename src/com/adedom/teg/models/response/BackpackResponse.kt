package com.adedom.teg.models.response

import com.adedom.teg.data.models.BackpackDb

data class BackpackResponse(
    var success: Boolean = false,
    var message: String? = null,
    var backpack: BackpackDb? = null,
)
