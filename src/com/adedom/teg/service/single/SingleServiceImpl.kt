package com.adedom.teg.service.single

import com.adedom.teg.controller.single.model.ItemCollectionRequest
import com.adedom.teg.repositories.TegRepository
import com.adedom.teg.response.BackpackResponse
import com.adedom.teg.response.BaseResponse
import com.adedom.teg.util.TegConstant
import com.adedom.teg.util.toMessageIncorrect
import com.adedom.teg.util.toMessageIsNullOrBlank
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
class SingleServiceImpl(private val repository: TegRepository) : SingleService {

    override fun fetchItemCollection(playerId: String): BackpackResponse {
        return BackpackResponse()
    }

    override fun itemCollection(playerId: String?, itemCollectionRequest: ItemCollectionRequest): BaseResponse {
        val response = BaseResponse()
        val (itemId, qty, latitude, longitude) = itemCollectionRequest

        val message: String = when {
            // validate Null Or Blank
            playerId.isNullOrBlank() -> playerId.toMessageIsNullOrBlank()
            itemId == null -> itemCollectionRequest::itemId.name.toMessageIsNullOrBlank()
            qty == null -> itemCollectionRequest::qty.name.toMessageIsNullOrBlank()
            latitude == null -> itemCollectionRequest::latitude.name.toMessageIsNullOrBlank()
            longitude == null -> itemCollectionRequest::longitude.name.toMessageIsNullOrBlank()

            // validate values of variable
            itemId <= 0 || itemId > TegConstant.MAX_ITEM -> itemCollectionRequest::itemId.name.toMessageIncorrect()
            qty <= 0 -> itemCollectionRequest::qty.name.toMessageIncorrect()

            // validate database

            // execute
            else -> {
                response.success = repository.itemCollection(playerId, itemCollectionRequest)
                "Post item collection success"
            }
        }

        response.message = message
        return response
    }

}
