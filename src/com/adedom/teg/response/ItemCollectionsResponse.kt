package com.adedom.teg.response

import com.adedom.teg.models.ItemCollection

data class ItemCollectionsResponse(
    val itemCollection: List<ItemCollection>? = null
) : BaseResponse()
