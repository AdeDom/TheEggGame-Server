package com.adedom.teg.http.models.response

import com.adedom.teg.refactor.Backpack

data class BackpackResponse(
    var success: Boolean = false,
    var message: String? = null,
    var backpack: Backpack? = null,
)
