package com.adedom.teg.business.multi

import com.adedom.teg.business.business.TegBusiness
import com.adedom.teg.business.jwtconfig.JwtConfig
import com.adedom.teg.data.repositories.TegRepository
import com.adedom.teg.models.request.ChangeTeamRequest
import com.adedom.teg.models.request.CreateRoomRequest
import com.adedom.teg.models.request.JoinRoomInfoRequest
import com.adedom.teg.models.request.MultiItemCollectionRequest
import com.adedom.teg.models.response.*
import com.adedom.teg.models.websocket.RoomInfoPlayers
import com.adedom.teg.models.websocket.RoomInfoPlayersOutgoing
import com.adedom.teg.models.websocket.RoomInfoTitleOutgoing
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
                response.success = repository.multiItemCollection(
                    playerId,

                    MultiItemCollectionRequest(
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
                        people = it.people,
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
                    people = roomDb.people,
                    status = roomDb.status,
                    startTime = roomDb.startTime,
                    endTime = roomDb.endTime,
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

    override fun fetchRoomInfoPlayers(roomNo: String): RoomInfoPlayersOutgoing {
        val response = RoomInfoPlayersOutgoing()

        val message: String = when {
            // validate Null Or Blank

            // validate values of variable

            // validate database

            // execute
            else -> {
                val playerInfoList = repository.fetchRoomInfoPlayers(roomNo).map {
                    RoomInfoPlayers(
                        playerId = it.playerId,
                        username = it.username,
                        name = it.name?.capitalize(),
                        image = it.image,
                        level = business.toConvertLevel(it.level),
                        state = it.state,
                        gender = it.gender,
                        birthDate = business.toConvertDateTimeLongToString(it.birthDate),
                        roleRoomInfo = it.roleRoomInfo,
                        statusRoomInfo = it.statusRoomInfo,
                        teamRoomInfo = it.teamRoomInfo,
                    )
                }
                response.roomInfoPlayers = playerInfoList

                response.roomNo = roomNo

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

    override fun changeTeam(playerId: String?, changeTeamRequest: ChangeTeamRequest): BaseResponse {
        val response = BaseResponse()
        val (team) = changeTeamRequest

        val message: String = when {
            // validate Null Or Blank
            playerId.isNullOrBlank() -> business.toMessageIsNullOrBlank(playerId)
            team.isNullOrBlank() -> business.toMessageIsNullOrBlank(changeTeamRequest::team)

            // validate values of variable
            !business.isValidateTeam(team) -> business.toMessageIncorrect(changeTeamRequest::team)

            // validate database

            // execute
            else -> {
                response.success = repository.changeTeam(playerId, changeTeamRequest)
                "Change team success"
            }
        }

        response.message = message
        return response
    }

    override fun changeStatusRoomInfo(playerId: String?): BaseResponse {
        val response = BaseResponse()

        val message: String = when {
            // validate Null Or Blank
            playerId.isNullOrBlank() -> business.toMessageIsNullOrBlank(playerId)

            // validate values of variable

            // validate database

            // execute
            else -> {
                response.success = repository.changeStatusRoomInfo(playerId)
                "Change status room info success"
            }
        }

        response.message = message
        return response
    }

    override fun roomInfoTegMulti(playerId: String?): BaseResponse {
        val response = BaseResponse()

        val roomNo = playerId?.let { repository.currentRoomNo(it) }

        val message: String = when {
            // validate Null Or Blank
            playerId.isNullOrBlank() -> business.toMessageIsNullOrBlank(playerId)
            roomNo.isNullOrBlank() -> business.toMessageIsNullOrBlank(roomNo)

            // validate values of variable

            // validate database
            repository.isValidateTegMultiPeople(roomNo) -> business.toMessageTegMultiPeople()
            repository.isValidateTegMultiTeam(roomNo) -> business.toMessageTegMultiTeam()
            repository.isValidateTegMultiStatus(roomNo) -> business.toMessageTegMultiStatus()

            // execute
            else -> {
                response.success = repository.roomInfoTegMulti(playerId, roomNo)
                "Room info teg multi success"
            }
        }

        response.message = message
        return response
    }

    override fun changeStatusUnready(playerId: String?): BaseResponse {
        val response = BaseResponse()

        val message: String = when {
            // validate Null Or Blank
            playerId.isNullOrBlank() -> business.toMessageIsNullOrBlank(playerId)

            // validate values of variable

            // validate database

            // execute
            else -> {
                response.success = repository.changeStatusUnready(playerId)
                "Change status unready success"
            }
        }

        response.message = message
        return response
    }

    override fun fetchMultiPlayer(playerId: String?): FetchMultiPlayerResponse {
        val response = FetchMultiPlayerResponse()

        val message: String = when {
            // validate Null Or Blank
            playerId.isNullOrBlank() -> business.toMessageIsNullOrBlank(playerId)

            // validate values of variable

            // validate database

            // execute
            else -> {
                val roomNo = repository.currentRoomNo(playerId)

                val roomDb = repository.fetchRoomInfoTitle(roomNo)
                val fetchRoomResponse = FetchRoomResponse(
                    roomId = roomDb.roomId,
                    roomNo = roomDb.roomNo,
                    name = roomDb.name?.capitalize(),
                    people = roomDb.people,
                    status = roomDb.status,
                    startTime = roomDb.startTime,
                    endTime = roomDb.endTime,
                    dateTime = business.toConvertDateTimeLongToString(roomDb.dateTime),
                )
                response.roomInfoTitle = fetchRoomResponse

                val playerInfoList = repository.fetchRoomInfoPlayers(roomNo).map {
                    RoomInfoPlayers(
                        playerId = it.playerId,
                        username = it.username,
                        name = it.name?.capitalize(),
                        image = it.image,
                        level = business.toConvertLevel(it.level),
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
                "Fetch multi player success"
            }
        }

        response.message = message
        return response
    }

    override fun fetchMultiScore(playerId: String?): ScoreResponse {
        val response = ScoreResponse()

        val message: String = when {
            // validate Null Or Blank
            playerId.isNullOrBlank() -> business.toMessageIsNullOrBlank(playerId)

            // validate values of variable

            // validate database

            // execute
            else -> {
                response.score = repository.fetchMultiScore(playerId)
                response.success = true
                "Fetch multi score success"
            }
        }

        response.message = message
        return response
    }

    override fun addMultiScore(playerId: String?): BaseResponse {
        val response = BaseResponse()

        val message: String = when {
            // validate Null Or Blank
            playerId.isNullOrBlank() -> business.toMessageIsNullOrBlank(playerId)

            // validate values of variable

            // validate database

            // execute
            else -> {
                response.success = repository.addMultiScore(playerId)
                "Add multi score success"
            }
        }

        response.message = message
        return response
    }

    override fun fetchMultiItem(playerId: String?): MultiItemResponse {
        val response = MultiItemResponse()

        val message: String = when {
            // validate Null Or Blank
            playerId.isNullOrBlank() -> business.toMessageIsNullOrBlank(playerId)

            // validate values of variable

            // validate database

            // execute
            else -> {
                response.multiItems = repository.fetchMultiItem(playerId)

                response.success = true
                "Fetch multi item success"
            }
        }

        response.message = message
        return response
    }

    override fun addMultiItem(playerId: String?): BaseResponse {
        val response = BaseResponse()

        val roomNo = playerId?.let { repository.currentRoomNo(it) }

        val message: String = when {
            // validate Null Or Blank
            playerId.isNullOrBlank() -> business.toMessageIsNullOrBlank(playerId)
            roomNo.isNullOrBlank() -> business.toMessageIsNullOrBlank(roomNo)

            // validate values of variable

            // validate database

            // execute
            else -> {
                response.success = repository.addMultiItem(playerId, roomNo)
                "Add multi item success"
            }
        }

        response.message = message
        return response
    }

}
