package com.adedom.teg.business.multi

import com.adedom.teg.business.business.TegBusiness
import com.adedom.teg.business.jwtconfig.JwtConfig
import com.adedom.teg.data.repositories.TegRepository
import com.adedom.teg.models.request.CreateRoomRequest
import com.adedom.teg.models.request.ItemCollectionRequest
import com.adedom.teg.models.request.JoinRoomInfoRequest
import com.adedom.teg.models.request.MultiItemCollectionRequest
import com.adedom.teg.models.response.BaseResponse
import com.adedom.teg.models.response.FetchRoomResponse
import com.adedom.teg.models.response.CurrentRoomNoResponse
import com.adedom.teg.models.response.RoomsResponse
import com.adedom.teg.models.websocket.RoomInfoPlayers
import com.adedom.teg.models.websocket.RoomInfoPlayersOutgoing
import com.adedom.teg.models.websocket.RoomInfoTitleOutgoing
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

    override fun createRoom(playerId: String?, createRoomRequest: CreateRoomRequest): BaseResponse {
        val response = BaseResponse()
        val (roomName, roomPeople) = createRoomRequest

        val message: String = when {
            // validate Null Or Blank
            playerId.isNullOrBlank() -> business.toMessageIsNullOrBlank(playerId)
            roomName.isNullOrBlank() -> business.toMessageIsNullOrBlank(createRoomRequest::roomName)
            roomPeople == null -> business.toMessageIsNullOrBlank1(createRoomRequest::roomPeople)

            // validate values of variable
            business.isValidateRoomPeople(roomPeople) -> business.toMessageIncorrect1(createRoomRequest::roomPeople)

            // validate database

            // execute
            else -> {
                response.success = repository.createRoom(playerId, createRoomRequest)
                "Create room success"
            }
        }

        response.message = message
        return response
    }

    override fun fetchRoomInfoTitle(roomNo: String): RoomInfoTitleOutgoing {
        val response = RoomInfoTitleOutgoing()

        val message: String = when {
            // validate Null Or Blank

            // validate values of variable

            // validate database

            // execute
            else -> {
                val roomDb = repository.fetchRoomInfoTitle(roomNo)
                val fetchRoomResponse = FetchRoomResponse(
                    roomId = roomDb.roomId,
                    roomNo = roomDb.roomNo,
                    name = roomDb.name?.capitalize(),
                    people = roomDb.people?.toInt(),
                    status = roomDb.status,
                    dateTime = business.toConvertDateTimeLongToString(roomDb.dateTime),
                )
                response.roomInfoTitle = fetchRoomResponse

                response.roomNo = roomNo

                response.success = true
                "Fetch room info success"
            }
        }

        response.message = message
        return response
    }

    override fun fetchRoomInfoPlayers(accessToken: String?): RoomInfoPlayersOutgoing {
        val response = RoomInfoPlayersOutgoing()

        val message: String = when {
            // validate Null Or Blank
            accessToken.isNullOrBlank() -> business.toMessageIsNullOrBlank(accessToken)

            // validate values of variable
            business.isValidateJwtIncorrect(accessToken, jwtConfig.playerId) -> business.toMessageIncorrect(accessToken)

            // validate database

            // execute
            else -> {
                val playerId = jwtConfig.decodeJwtGetPlayerId(accessToken)
                val playerInfoList = repository.fetchRoomInfoPlayers(playerId).map {
                    RoomInfoPlayers(
                        playerId = it.playerId,
                        username = it.username,
                        name = it.name?.capitalize(),
                        image = it.image,
                        level = it.level,
                        state = it.state,
                        gender = it.gender,
                        birthDate = business.toConvertDateTimeLongToString(it.birthDate),
                        roleRoomInfo = it.roleRoomInfo,
                        statusRoomInfo = it.statusRoomInfo,
                        teamRoomInfo = it.teamRoomInfo,
                    )
                }
                response.roomInfoPlayers = playerInfoList

                response.success = true
                "Fetch room info success"
            }
        }

        response.message = message
        return response
    }

    override fun currentRoomNo(playerId: String?): CurrentRoomNoResponse {
        val response = CurrentRoomNoResponse()

        val message: String = when {
            // validate Null Or Blank
            playerId.isNullOrBlank() -> business.toMessageIsNullOrBlank(playerId)

            // validate values of variable

            // validate database

            // execute
            else -> {
                response.success = true
                response.roomNo = repository.currentRoomNo(playerId)
                "Fetch room no current success"
            }
        }

        response.message = message
        return response
    }

    override fun currentRoomNo(accessToken: String): String {
        val playerId = jwtConfig.decodeJwtGetPlayerId(accessToken)
        return repository.currentRoomNo(playerId)
    }

    override fun joinRoomInfo(playerId: String?, joinRoomInfoRequest: JoinRoomInfoRequest): BaseResponse {
        val response = BaseResponse()
        val (roomNo) = joinRoomInfoRequest

        val message: String = when {
            // validate Null Or Blank
            playerId.isNullOrBlank() -> business.toMessageIsNullOrBlank(playerId)
            roomNo.isNullOrBlank() -> business.toMessageIsNullOrBlank(joinRoomInfoRequest::roomNo)

            // validate values of variable

            // validate database
            repository.isValidateRoomNoOnReady(roomNo) -> business.toMessageIncorrect(joinRoomInfoRequest::roomNo)
            repository.isValidatePeopleRoomInfo(roomNo) -> business.toMessagePeopleRoomInfo()

            // execute
            else -> {
                response.success = repository.joinRoomInfo(playerId, joinRoomInfoRequest)
                "Join room info success"
            }
        }

        response.message = message
        return response
    }

    override fun leaveRoomInfo(playerId: String?): BaseResponse {
        val response = BaseResponse()

        val message: String = when {
            // validate Null Or Blank
            playerId.isNullOrBlank() -> business.toMessageIsNullOrBlank(playerId)

            // validate values of variable

            // validate database

            // execute
            else -> {
                response.success = repository.leaveRoomInfo(playerId)
                "Leave room info success"
            }
        }

        response.message = message
        return response
    }

}
