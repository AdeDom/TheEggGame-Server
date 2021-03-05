package com.adedom.teg.business.multi

import com.adedom.teg.business.business.TegBusiness
import com.adedom.teg.business.jwtconfig.JwtConfig
import com.adedom.teg.data.repositories.TegRepository
import com.adedom.teg.models.TegLatLng
import com.adedom.teg.models.request.*
import com.adedom.teg.models.response.*
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
        val (qty) = multiItemCollectionRequest

        val message: String = when {
            // validate Null Or Blank
            playerId.isNullOrBlank() -> business.toMessageIsNullOrBlank(playerId)
            qty == null -> business.toMessageIsNullOrBlank1(multiItemCollectionRequest::qty)

            // validate values of variable
            business.isValidateLessThanOrEqualToZero(qty) -> business.toMessageIncorrect1(multiItemCollectionRequest::qty)

            // validate database

            // execute
            else -> {
                response.success = repository.multiItemCollection(playerId, MultiItemCollectionRequest(qty))
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
                    name = roomDb.name.capitalize(),
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
                        name = it.name.capitalize(),
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
                    name = roomDb.name.capitalize(),
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
                        name = it.name.capitalize(),
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

    override fun addMultiScore(playerId: String?, addMultiScoreRequest: AddMultiScoreRequest): BaseResponse {
        val response = BaseResponse()
        val (multiId) = addMultiScoreRequest

        val message: String = when {
            // validate Null Or Blank
            playerId.isNullOrBlank() -> business.toMessageIsNullOrBlank(playerId)
            multiId == null -> business.toMessageIsNullOrBlank1(addMultiScoreRequest::multiId)

            // validate values of variable

            // validate database
            repository.isValidateMultiItemId(multiId) -> business.toMessageIncorrect1(addMultiScoreRequest::multiId)
            repository.isValidateMultiItemStatusIncorrect(multiId) -> business.toMessageIncorrect1(addMultiScoreRequest::multiId)

            // execute
            else -> {
                response.success = repository.addMultiScore(playerId, multiId)
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
            repository.isValidateHeadRoomInfo(playerId) -> business.toMessageIncorrect(playerId)

            // execute
            else -> {
                val currentPlayer = repository.currentPlayer(playerId)
                repeat(TegConstant.MULTI_PLAYER_MAX_ITEM) {
                    val latLng = business.generateMultiItem(currentPlayer)
                    repository.addMultiItem(roomNo, latLng.latitude, latLng.longitude)
                }

                repository.fetchLocationOtherPlayer(roomNo).forEach { latLngPlayer ->
                    val locationPlayer = TegLatLng(latLngPlayer.latitude, latLngPlayer.longitude)

                    var countItem = 0
                    repository.fetchMultiItem(playerId).forEach { latLngItem ->
                        val locationItem = TegLatLng(latLngItem.latitude, latLngItem.longitude)

                        val distance = business.distanceBetween(locationPlayer, locationItem)
                        if (distance < TegConstant.THREE_HUNDRED_METER) {
                            countItem++
                        }
                    }

                    countItem = if (countItem > TegConstant.MULTI_PLAYER_MAX_ITEM) {
                        TegConstant.MULTI_PLAYER_MAX_ITEM
                    } else {
                        countItem
                    }
                    repeat(TegConstant.MULTI_PLAYER_MAX_ITEM - countItem) {
                        val latLng = business.generateMultiItem(locationPlayer)
                        repository.addMultiItem(roomNo, latLng.latitude, latLng.longitude)
                    }
                }

                response.success = true
                "Add multi item success"
            }
        }

        response.message = message
        return response
    }

    override fun fetchMultiPlayerEndGame(playerId: String?): MultiPlayerEndGameResponse {
        val response = MultiPlayerEndGameResponse()

        val message: String = when {
            // validate Null Or Blank
            playerId.isNullOrBlank() -> business.toMessageIsNullOrBlank(playerId)

            // validate values of variable

            // validate database

            // execute
            else -> {
                val (scoreTeamA, scoreTeamB) = repository.fetchMultiScore(playerId)
                val team = repository.currentTeam(playerId)

                val triple = business.multiPlayerEndGame(scoreTeamA, scoreTeamB, team)

                val multiPlayerEndGame = MultiPlayerEndGame(
                    scoreTeamA = scoreTeamA,
                    scoreTeamB = scoreTeamB,
                    resultTeamA = triple?.first,
                    resultTeamB = triple?.second,
                    isBonusEndGame = triple?.third ?: false,
                )
                response.endGame = multiPlayerEndGame

                response.success = true
                "Fetch multi player end game success"
            }
        }

        response.message = message
        return response
    }

    override fun multiPlayerEndGame(playerId: String?): BaseResponse {
        val response = BaseResponse()

        val message: String = when {
            // validate Null Or Blank
            playerId.isNullOrBlank() -> business.toMessageIsNullOrBlank(playerId)

            // validate values of variable

            // validate database

            // execute
            else -> {
                val roomNo = repository.currentRoomNo(playerId)
                response.success = repository.multiPlayerEndGame(roomNo)
                "Multi player end game"
            }
        }

        response.message = message
        return response
    }

}
