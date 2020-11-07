package com.adedom.teg.business.multi

import com.adedom.teg.business.business.TegBusiness
import com.adedom.teg.business.jwtconfig.JwtConfig
import com.adedom.teg.data.repositories.TegRepository
import com.adedom.teg.models.request.ItemCollectionRequest
import com.adedom.teg.models.request.MultiItemCollectionRequest
import com.adedom.teg.models.response.BaseResponse
import com.adedom.teg.models.response.FetchRoomResponse
import com.adedom.teg.models.response.RoomsResponse
import com.adedom.teg.models.websocket.CreateRoomIncoming
import com.adedom.teg.util.TegConstant
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
class MultiServiceImpl(
    private val repository: TegRepository,
    private val business: TegBusiness,
    private val jwtConfig: JwtConfig,
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

    override fun fetchRooms(): RoomsResponse {
        val response = RoomsResponse()

        val message: String = when {
            // validate Null Or Blank

            // validate values of variable

            // validate database

            // execute
            else -> {
                val rooms = repository.fetchRooms().map {
                    FetchRoomResponse(
                        roomId = it.roomId,
                        roomNo = it.roomNo,
                        name = it.name,
                        people = it.people?.toInt(),
                        status = it.status,
                        dateTime = business.toConvertDateTimeLongToString(it.dateTime),
                    )
                }
                response.rooms = rooms
                response.success = true
                "Fetch rooms success"
            }
        }

        response.message = message
        return response
    }

    override fun createRoom(accessToken: String?, createRoomIncoming: CreateRoomIncoming): RoomsResponse {
        val response = RoomsResponse()
        val (roomName, roomPeople) = createRoomIncoming

        val message: String = when {
            // validate Null Or Blank
            accessToken.isNullOrBlank() -> business.toMessageIsNullOrBlank(accessToken)
            roomName.isNullOrBlank() -> business.toMessageIsNullOrBlank(createRoomIncoming::roomName)
            roomPeople == null -> business.toMessageIsNullOrBlank1(createRoomIncoming::roomPeople)

            // validate values of variable
            business.isValidateRoomPeople(roomPeople) -> business.toMessageIncorrect1(createRoomIncoming::roomPeople)

            // validate database

            // execute
            else -> {
                val playerId: String = jwtConfig.decodeJwtGetPlayerId(accessToken)

                response.success = repository.createRoom(playerId, createRoomIncoming)
                "Create room success"
            }
        }

        response.message = message
        return response
    }

}
