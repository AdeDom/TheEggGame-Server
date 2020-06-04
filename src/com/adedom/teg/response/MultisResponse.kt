package com.adedom.teg.response

import com.adedom.teg.models.Multi

data class MultisResponse(
    var multi: List<Multi>? = null
) : BaseResponse()
