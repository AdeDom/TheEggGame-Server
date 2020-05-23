package com.adedom.teg.response

import com.adedom.teg.models.Multi

data class MultisResponse(
    val multi: List<Multi>? = null
) : BaseResponse()
