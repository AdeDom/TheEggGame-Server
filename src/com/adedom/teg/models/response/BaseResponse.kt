package com.adedom.teg.models.response

data class BaseResponse(
    var success: Boolean = false,
    var message: String? = null,
)
