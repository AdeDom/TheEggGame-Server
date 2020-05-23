package com.adedom.teg.response

import com.adedom.teg.models.Multi

data class MultiResponse(
    val multi: List<Multi>? = null
) : BaseResponse()
