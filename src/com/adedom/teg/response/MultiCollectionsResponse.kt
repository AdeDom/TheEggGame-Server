package com.adedom.teg.response

import com.adedom.teg.models.MultiCollection

data class MultiCollectionsResponse(
    val multiCollection: List<MultiCollection>? = null
) : BaseResponse()
