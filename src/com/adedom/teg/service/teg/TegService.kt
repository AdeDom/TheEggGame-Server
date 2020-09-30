package com.adedom.teg.service.teg

import com.adedom.teg.request.single.ItemCollectionRequest
import com.adedom.teg.response.BackpackResponse
import com.adedom.teg.response.BaseResponse

interface TegService {

    fun fetchItemCollection(playerId: String): BackpackResponse

    fun postItemCollection(playerId: String, itemCollectionRequest: ItemCollectionRequest): BaseResponse

}
