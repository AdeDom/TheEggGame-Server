package com.adedom.teg.response

import com.adedom.teg.data.BASE_RESPONSE_MESSAGE

data class BaseResponse(
    var success: Boolean = false,
    var message: String? = BASE_RESPONSE_MESSAGE
)
