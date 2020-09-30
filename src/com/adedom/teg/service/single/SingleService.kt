package com.adedom.teg.service.single

import com.adedom.teg.controller.single.model.ItemCollectionRequest
import com.adedom.teg.response.BackpackResponse
import com.adedom.teg.response.BaseResponse
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
interface SingleService {

    fun fetchItemCollection(playerId: String): BackpackResponse

    fun itemCollection(playerId: String?, itemCollectionRequest: ItemCollectionRequest): BaseResponse

}
