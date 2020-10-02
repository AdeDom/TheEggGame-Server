package com.adedom.teg.http.models.response

import com.adedom.teg.refactor.Multi

data class MultisResponse(
    var success: Boolean = false,
    var message: String? = null,
    var multi: List<Multi>? = null,
)
