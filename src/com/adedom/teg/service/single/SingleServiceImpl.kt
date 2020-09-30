package com.adedom.teg.service.single

import com.adedom.teg.controller.single.model.ItemCollectionRequest
import com.adedom.teg.repositories.TegRepository
import com.adedom.teg.response.BackpackResponse
import com.adedom.teg.response.BaseResponse
import com.adedom.teg.service.business.TegBusiness
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
class SingleServiceImpl(
    private val repository: TegRepository,
    private val business: TegBusiness,
) : SingleService {

    override fun fetchItemCollection(playerId: String?): BackpackResponse {
        val response = BackpackResponse()

        val message: String = when {
            // validate Null Or Blank
            playerId.isNullOrBlank() -> business.toMessageIsNullOrBlank(playerId)

            // validate values of variable

            // validate database

            // execute
            else -> {
                response.success = true
                response.backpack = repository.fetchItemCollection(playerId)
                "Fetch back pack success"
            }
        }

        response.message = message
        return response
    }

    override fun itemCollection(playerId: String?, itemCollectionRequest: ItemCollectionRequest): BaseResponse {
        val response = BaseResponse()
        val (itemId, qty, latitude, longitude) = itemCollectionRequest

        val message: String = when {
            // validate Null Or Blank
            playerId.isNullOrBlank() -> business.toMessageIsNullOrBlank(playerId)
            itemId == null -> business.toMessageIsNullOrBlank1(itemCollectionRequest::itemId)
            qty == null -> business.toMessageIsNullOrBlank1(itemCollectionRequest::qty)
            latitude == null -> business.toMessageIsNullOrBlank2(itemCollectionRequest::latitude)
            longitude == null -> business.toMessageIsNullOrBlank2(itemCollectionRequest::longitude)

            // validate values of variable
            itemId <= 0 -> business.toMessageIncorrect1(itemCollectionRequest::itemId)
            qty <= 0 -> business.toMessageIncorrect1(itemCollectionRequest::qty)

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
