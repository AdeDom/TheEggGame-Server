package com.adedom.teg.business.multi

import com.adedom.teg.business.business.TegBusiness
import com.adedom.teg.data.repositories.TegRepository
import com.adedom.teg.models.request.ItemCollectionRequest
import com.adedom.teg.models.request.MultiItemCollectionRequest
import com.adedom.teg.models.response.BaseResponse
import com.adedom.teg.util.TegConstant
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
class MultiServiceImpl(
    private val repository: TegRepository,
    private val business: TegBusiness,
) : MultiService {

    override fun itemCollection(
        playerId: String?,
        multiItemCollectionRequest: MultiItemCollectionRequest
    ): BaseResponse {
        val response = BaseResponse()
        val (itemId, qty, latitude, longitude) = multiItemCollectionRequest

        val message: String = when {
            // validate Null Or Blank
            playerId.isNullOrBlank() -> business.toMessageIsNullOrBlank(playerId)
            itemId == null -> business.toMessageIsNullOrBlank1(multiItemCollectionRequest::itemId)
            qty == null -> business.toMessageIsNullOrBlank1(multiItemCollectionRequest::qty)
            latitude == null -> business.toMessageIsNullOrBlank2(multiItemCollectionRequest::latitude)
            longitude == null -> business.toMessageIsNullOrBlank2(multiItemCollectionRequest::longitude)

            // validate values of variable
            business.isValidateLessThanOrEqualToZero(itemId) -> business.toMessageIncorrect1(multiItemCollectionRequest::itemId)
            business.isValidateLessThanOrEqualToZero(qty) -> business.toMessageIncorrect1(multiItemCollectionRequest::qty)

            // validate database

            // execute
            else -> {
                response.success = repository.itemCollection(
                    playerId,
                    TegConstant.ITEM_COLLECTION_MULTI,

                    // TODO: 25/10/2563 multi item collection
                    ItemCollectionRequest(
                        itemId = itemId,
                        qty = qty,
                        latitude = latitude,
                        longitude = longitude,
                    )
                )
                "Post multi item collection success"
            }
        }

        response.message = message
        return response
    }

}
