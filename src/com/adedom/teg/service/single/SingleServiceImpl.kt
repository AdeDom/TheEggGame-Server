package com.adedom.teg.service.single

import com.adedom.teg.controller.single.model.ItemCollectionRequest
import com.adedom.teg.repositories.TegRepository
import com.adedom.teg.response.BackpackResponse
import com.adedom.teg.response.BaseResponse
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
class SingleServiceImpl(private val repository: TegRepository) : SingleService {

    override fun fetchItemCollection(playerId: String): BackpackResponse {
        return BackpackResponse()
    }

    override fun postItemCollection(playerId: String, itemCollectionRequest: ItemCollectionRequest): BaseResponse {
        return BaseResponse()
    }

}
