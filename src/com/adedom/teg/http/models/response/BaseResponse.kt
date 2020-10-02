package com.adedom.teg.http.models.response

data class BaseResponse(
    var success: Boolean = false,
    var message: String? = null,
)
