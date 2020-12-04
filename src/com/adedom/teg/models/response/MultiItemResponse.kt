package com.adedom.teg.models.response

import com.adedom.teg.data.models.MultiItemDb

data class MultiItemResponse(
    var success: Boolean = false,
    var message: String? = null,
    var multiItems: List<MultiItemDb> = emptyList(),
)
