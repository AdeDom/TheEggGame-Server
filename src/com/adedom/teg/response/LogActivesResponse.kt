package com.adedom.teg.response

import com.adedom.teg.models.LogActive

data class LogActivesResponse(
    val logActive: List<LogActive>? = null
) : BaseResponse()
