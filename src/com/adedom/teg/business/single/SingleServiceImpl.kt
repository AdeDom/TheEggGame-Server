package com.adedom.teg.business.single

import com.adedom.teg.business.business.TegBusiness
import com.adedom.teg.business.jwtconfig.JwtConfig
import com.adedom.teg.data.repositories.TegRepository
import com.adedom.teg.models.TegLatLng
import com.adedom.teg.models.request.SingleItemRequest
import com.adedom.teg.models.response.BackpackResponse
import com.adedom.teg.models.response.BaseResponse
import com.adedom.teg.models.response.PlayerInfo
import com.adedom.teg.models.websocket.PlaygroundSinglePlayerOutgoing
import com.adedom.teg.models.websocket.SingleItemOutgoing
import com.adedom.teg.models.websocket.SingleSuccessAnnouncementOutgoing
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

    // TODO: 16/11/2563 validate distance
    override fun itemCollection(playerId: String?, singleItemRequest: SingleItemRequest): BaseResponse {
        val response = BaseResponse()
        val (singleId) = singleItemRequest

        val message: String = when {
            // validate Null Or Blank
            playerId.isNullOrBlank() -> business.toMessageIsNullOrBlank(playerId)
            singleId == null -> business.toMessageIsNullOrBlank1(singleItemRequest::singleId)

            // validate values of variable

            // validate database
            repository.isValidateSingleItemId(singleId) -> business.toMessageIncorrect1(singleItemRequest::singleId)
            repository.isValidateSingleItemStatusIncorrect(singleId) -> business.toMessageIncorrect1(singleItemRequest::singleId)

            // execute
            else -> {
                val singleItemDb = repository.getSingleItemDb(singleId)

                response.success = repository.singleItemCollection(
                    playerId,
                    singleItemRequest,
                    business.randomSingleItemCollection(singleItemDb.itemTypeId),
                    TegLatLng(singleItemDb.latitude ?: 0.0, singleItemDb.longitude ?: 0.0),
                )
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
                val currentLatLng = TegLatLng(latitude ?: 0.0, longitude ?: 0.0)

                val singleItems = repository.fetchSingleItem()

                repeat(business.addSingleItemTimes(currentLatLng, singleItems)) {
                    repository.addSingleItem(playerId, business.generateSingleItem(currentLatLng))
                }
            }
        }

        return SingleItemOutgoing(repository.fetchSingleItem())
    }

    override fun singleSuccessAnnouncement(accessToken: String): SingleSuccessAnnouncementOutgoing {
        val playerId = jwtConfig.decodeJwtGetPlayerId(accessToken)

        return repository.fetchSingleSuccessAnnouncement(playerId)
    }

    override fun fetchPlaygroundSinglePlayer(): PlaygroundSinglePlayerOutgoing {
        val players = repository.fetchPlaygroundSinglePlayer().map {
            PlayerInfo(
                playerId = it.playerId,
                username = it.username,
                name = it.name?.capitalize(),
                image = it.image,
                level = business.toConvertLevel(it.level),
                state = it.state,
                gender = it.gender,
                birthDate = business.toConvertDateTimeLongToString(it.birthDate),
                latitude = it.latitude,
                longitude = it.longitude,
            )
        }

        return PlaygroundSinglePlayerOutgoing(players)
    }

}
