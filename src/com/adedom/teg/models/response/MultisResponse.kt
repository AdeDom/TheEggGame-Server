package com.adedom.teg.models.response

import com.adedom.teg.data.models.MultiDb

data class MultisResponse(
    var success: Boolean = false,
    var message: String? = null,
    var multi: List<MultiDb>? = null,
)
