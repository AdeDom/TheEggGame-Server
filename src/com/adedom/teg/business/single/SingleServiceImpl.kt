package com.adedom.teg.business.single

import com.adedom.teg.business.business.TegBusiness
import com.adedom.teg.business.jwtconfig.JwtConfig
import com.adedom.teg.data.repositories.TegRepository
import com.adedom.teg.models.request.SingleItemRequest
import com.adedom.teg.models.response.BackpackResponse
import com.adedom.teg.models.response.BaseResponse
import com.adedom.teg.models.websocket.SingleItemOutgoing
import com.adedom.teg.util.LatLng
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
class SingleServiceImpl(
    private val repository: TegRepository,
    private val business: TegBusiness,
    private val jwtConfig: JwtConfig,
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

    override fun itemCollection(playerId: String?, singleItemRequest: SingleItemRequest): BaseResponse {
        val response = BaseResponse()
        val (singleId) = singleItemRequest

        val message: String = when {
            // validate Null Or Blank
            playerId.isNullOrBlank() -> business.toMessageIsNullOrBlank(playerId)
            singleId == null -> business.toMessageIsNullOrBlank1(singleItemRequest::singleId)

            // validate values of variable

            // validate database

            // execute
            else -> {
                response.success = repository.singleItemCollection(playerId, singleItemRequest)
                "Post item collection success"
            }
        }

        response.message = message
        return response
    }

    override fun singleItem(accessToken: String): SingleItemOutgoing {
        when {
            business.isValidateJwtExpires(accessToken) -> business.toMessageIncorrect(accessToken)
            business.isValidateJwtIncorrect(accessToken, jwtConfig.playerId) -> business.toMessageIncorrect(accessToken)
            else -> {
                val playerId = jwtConfig.decodeJwtGetPlayerId(accessToken)

                val (latitude, longitude) = repository.getCurrentLatLngPlayer(playerId)

                if (latitude != null && longitude != null) {
                    val currentLatLng = LatLng(latitude, longitude)

                    val singleItems = repository.fetchSingleItem()

                    repeat(business.addSingleItemTimes(currentLatLng, singleItems)) {
                        repository.addSingleItem(playerId, business.generateSingleItem(currentLatLng))
                    }
                }
            }
        }

        return SingleItemOutgoing(repository.fetchSingleItem())
    }

}
