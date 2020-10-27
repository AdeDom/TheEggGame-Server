package com.adedom.teg.refactor

import com.adedom.teg.models.request.*
import com.adedom.teg.models.response.*
import com.adedom.teg.util.*
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*

fun Route.getMultiScore() {

    route("score") {
        get("fetch-score{${GetConstant.ROOM_NO}}") {
            val response = ScoreResponse()
            val roomNo = call.parameters[GetConstant.ROOM_NO]
            val message = when {
                roomNo.isNullOrBlank() -> GetConstant.ROOM_NO.validateIsNullOrBlank()
                roomNo.toInt() <= 0 -> GetConstant.ROOM_NO.validateLessEqZero()
                DatabaseTransaction.validateRoom(roomNo) -> GetConstant.ROOM_NO.validateIncorrect()

                else -> {
                    val score = DatabaseTransaction.getMultiScore(roomNo)
                    response.score = score
                    response.success = true
                    "Fetch multi score success"
                }
            }
            response.message = message
            call.respond(response)
        }
    }

}

fun Route.room() {

    route("room") {
        get("/") {
            val response = RoomsResponse()
            val message = when {
                else -> {
                    val listRoom = DatabaseTransaction.getRooms()
//                    response.rooms = listRoom
                    response.success = true
                    "Fetch rooms success"
                }
            }
            response.message = message
            call.respond(response)
        }

        post("/") {
            val response = RoomResponse()
            val (name, people, playerId) = call.receive<RoomRequest>()
            val message = when {
                name.isNullOrBlank() -> RoomRequest::name.name.validateIsNullOrBlank()

                people.isNullOrBlank() -> RoomRequest::people.name.validateIsNullOrBlank()
                people.toInt() < TegConstant.MIN_PEOPLE || people.toInt() > TegConstant.MAX_PEOPLE -> RoomRequest::people.name.validateIncorrect()

                playerId == null -> RoomRequest::playerId.name.validateIsNullOrBlank()
                DatabaseTransaction.validatePlayer(playerId) -> RoomRequest::playerId.name.validateNotFound()

                else -> {
                    val roomNo = DatabaseTransaction.postRoom(
                        roomRequest = RoomRequest(
                            name = name,
                            people = people,
                            playerId = playerId
                        )
                    )
                    response.roomNo = roomNo
                    response.success = true
                    "Post room success"
                }
            }
            response.message = message
            call.respond(response)
        }
    }

}

fun Route.roomInfo() {

    route("room-info") {
        get("fetch-room-info{${GetConstant.ROOM_NO}}") {
            val response = RoomInfoResponse()
            val roomNo = call.parameters[GetConstant.ROOM_NO]
            val message = when {
                roomNo.isNullOrBlank() -> GetConstant.ROOM_NO.validateIsNullOrBlank()
                roomNo.toInt() <= 0 -> GetConstant.ROOM_NO.validateLessEqZero()
                DatabaseTransaction.validateRoom(roomNo) -> GetConstant.ROOM_NO.validateIncorrect()

                else -> {
                    val listRoomInfo = DatabaseTransaction.getRoomInfos(roomNo)
                    response.roomInfo = listRoomInfo
                    response.success = true
                    "Fetch room info success"
                }
            }
            response.message = message
            call.respond(response)
        }

        post("/") {
            val response = BaseResponse()
            val (roomNo, playerId) = call.receive<RoomInfoRequest>()
            val message = when {
                roomNo.isNullOrBlank() -> RoomInfoRequest::roomNo.name.validateIsNullOrBlank()
                roomNo.toInt() <= 0 -> RoomInfoRequest::roomNo.name.validateLessEqZero()
                DatabaseTransaction.validateRoomInfo(roomNo) -> RoomInfoRequest::roomNo.name.validateNotFound()
                DatabaseTransaction.validatePeopleRoom(roomNo) -> "People in room is maximum"

                playerId == null -> RoomInfoRequest::playerId.name.validateIsNullOrBlank()
                DatabaseTransaction.validatePlayer(playerId) -> RoomInfoRequest::playerId.name.validateNotFound()

                else -> {
                    DatabaseTransaction.postRoomInfo(
                        roomInfoRequest = RoomInfoRequest(
                            roomNo = roomNo,
                            playerId = playerId
                        )
                    )
                    response.success = true
                    "Post room info success"
                }
            }
            response.message = message
            call.respond(response)
        }

        delete("/") {
            val response = BaseResponse()
            val (roomNo, playerId) = call.receive<RoomInfoRequest>()
            val message = when {
                roomNo.isNullOrBlank() -> RoomInfoRequest::roomNo.name.validateIsNullOrBlank()
                roomNo.toInt() <= 0 -> RoomInfoRequest::roomNo.name.validateLessEqZero()
                DatabaseTransaction.validateRoomInfo(roomNo) -> RoomInfoRequest::roomNo.name.validateNotFound()

                playerId == null -> RoomInfoRequest::playerId.name.validateIsNullOrBlank()
                DatabaseTransaction.validatePlayer(playerId) -> RoomInfoRequest::playerId.name.validateNotFound()

                else -> {
                    DatabaseTransaction.deletePlayerRoomInfo(
                        roomInfoRequest = RoomInfoRequest(
                            roomNo = roomNo,
                            playerId = playerId
                        )
                    )
                    response.success = true
                    "Delete room info success"
                }
            }
            response.message = message
            call.respond(response)
        }
    }

}

fun Route.multi() {

    route("multi") {
        get("fetch-multis{${GetConstant.ROOM_NO}}") {
            val response = MultisResponse()
            val roomNo = call.parameters[GetConstant.ROOM_NO]
            val message = when {
                roomNo.isNullOrBlank() -> GetConstant.ROOM_NO.validateIsNullOrBlank()
                roomNo.toInt() <= 0 -> GetConstant.ROOM_NO.validateIncorrect()
                DatabaseTransaction.validateMultiRoomNo(roomNo) -> GetConstant.ROOM_NO.validateNotFound()

                else -> {
                    val listMulti = DatabaseTransaction.getMultis(roomNo)
                    response.multi = listMulti
                    response.success = true
                    "Fetch multi success"
                }
            }
            response.message = message
            call.respond(response)
        }

        post("/") {
            val response = BaseResponse()
            val (roomNo, latitude, longitude) = call.receive<MultiRequest>()
            val message = when {
                roomNo.isNullOrBlank() -> MultiRequest::roomNo.name.validateIsNullOrBlank()
                roomNo.toInt() <= 0 -> MultiRequest::roomNo.name.validateLessEqZero()
                DatabaseTransaction.validateRoomInfo(roomNo) -> MultiRequest::roomNo.name.validateNotFound()

                latitude == null -> MultiRequest::latitude.name.validateIsNullOrBlank()

                longitude == null -> MultiRequest::longitude.name.validateIsNullOrBlank()

                else -> {
                    DatabaseTransaction.postMulti(
                        multiRequest = MultiRequest(
                            roomNo = roomNo,
                            latitude = latitude,
                            longitude = longitude
                        )
                    )
                    response.success = true
                    "Post multi success"
                }
            }
            response.message = message
            call.respond(response)
        }
    }

}

fun Route.postMultiCollection() {

    route("multi-collection") {
        post("/") {
            val response = BaseResponse()
            val (multiId, roomNo, playerId, team, latitude, longitude) = call.receive<MultiCollectionRequest>()
            val message = when {
                multiId == null -> MultiCollectionRequest::multiId.name.validateIsNullOrBlank()
                multiId <= 0 -> MultiCollectionRequest::multiId.name.validateLessEqZero()
                DatabaseTransaction.validateMulti(multiId) -> MultiCollectionRequest::multiId.name.validateNotFound()

                roomNo.isNullOrBlank() -> MultiCollectionRequest::roomNo.name.validateIsNullOrBlank()
                roomNo.toInt() <= 0 -> MultiCollectionRequest::roomNo.name.validateLessEqZero()
                DatabaseTransaction.validateRoomInfo(roomNo) -> MultiCollectionRequest::roomNo.name.validateNotFound()

                playerId == null -> MultiCollectionRequest::playerId.name.validateIsNullOrBlank()
                DatabaseTransaction.validatePlayer(playerId) -> MultiCollectionRequest::playerId.name.validateNotFound()

                team.isNullOrBlank() -> MultiCollectionRequest::team.name.validateIsNullOrBlank()
                !team.validateTeam() -> MultiCollectionRequest::team.name.validateIncorrect()

                latitude == null -> MultiCollectionRequest::latitude.name.validateIsNullOrBlank()

                longitude == null -> MultiCollectionRequest::longitude.name.validateIsNullOrBlank()

                else -> {
                    DatabaseTransaction.postMultiCollection(
                        multiCollectionRequest = MultiCollectionRequest(
                            multiId = multiId,
                            roomNo = roomNo,
                            playerId = playerId,
                            team = team,
                            latitude = latitude,
                            longitude = longitude
                        )
                    )
                    response.success = true
                    "Post multi collection success"
                }
            }
            response.message = message
            call.respond(response)
        }
    }

}

fun Route.putLatlng() {

    route("latlng") {
        put("/") {
            val response = BaseResponse()
            val (roomNo, playerId, latitude, longitude) = call.receive<LatlngRequest>()
            val message = when {
                roomNo.isNullOrBlank() -> LatlngRequest::roomNo.name.validateIsNullOrBlank()
                roomNo.toInt() <= 0 -> LatlngRequest::roomNo.name.validateLessEqZero()
                DatabaseTransaction.validateRoomInfo(roomNo) -> LatlngRequest::roomNo.name.validateNotFound()

                playerId == null -> LatlngRequest::playerId.name.validateIsNullOrBlank()
                DatabaseTransaction.validatePlayer(playerId) -> LatlngRequest::playerId.name.validateNotFound()

                latitude == null -> LatlngRequest::latitude.name.validateIsNullOrBlank()

                longitude == null -> LatlngRequest::longitude.name.validateIsNullOrBlank()

                else -> {
                    DatabaseTransaction.putLatLng(
                        latlngRequest = LatlngRequest(
                            roomNo = roomNo,
                            playerId = playerId,
                            latitude = latitude,
                            longitude = longitude
                        )
                    )
                    response.success = true
                    "Put latlng success"
                }
            }
            response.message = message
            call.respond(response)
        }
    }

}

fun Route.putReady() {

    route("ready") {
        put("/") {
            val response = BaseResponse()
            val (roomNo, playerId, status) = call.receive<ReadyRequest>()
            val message = when {
                roomNo.isNullOrBlank() -> ReadyRequest::roomNo.name.validateIsNullOrBlank()
                roomNo.toInt() <= 0 -> ReadyRequest::roomNo.name.validateLessEqZero()
                DatabaseTransaction.validateRoomInfo(roomNo) -> ReadyRequest::roomNo.name.validateNotFound()

                playerId == null -> ReadyRequest::playerId.name.validateIsNullOrBlank()
                DatabaseTransaction.validatePlayer(playerId) -> ReadyRequest::playerId.name.validateNotFound()

                status.isNullOrBlank() -> ReadyRequest::status.name.validateIsNullOrBlank()

                else -> {
                    DatabaseTransaction.putReady(
                        readyRequest = ReadyRequest(
                            roomNo = roomNo,
                            playerId = playerId,
                            status = status
                        )
                    )
                    response.success = true
                    "Put ready success"
                }
            }
            response.message = message
            call.respond(response)
        }
    }

}

fun Route.putRoomOff() {

    route("room-off") {
        put("/") {
            val response = BaseResponse()
            val (roomNo) = call.receive<RoomOffRequest>()
            val message = when {
                roomNo.isNullOrBlank() -> RoomOffRequest::roomNo.name.validateIsNullOrBlank()
                roomNo.toInt() <= 0 -> RoomOffRequest::roomNo.name.validateLessEqZero()
                DatabaseTransaction.validateRoom(roomNo) -> RoomOffRequest::roomNo.name.validateNotFound()
                else -> {
                    DatabaseTransaction.putRoomOff(roomNo)
                    response.success = true
                    "Put room off success"
                }
            }
            response.message = message
            call.respond(response)
        }
    }

}

fun Route.putTeam() {

    route("team") {
        put("/") {
            val response = BaseResponse()
            val (roomNo, playerId, team) = call.receive<TeamRequest>()
            val message = when {
                roomNo.isNullOrBlank() -> TeamRequest::roomNo.name.validateIsNullOrBlank()
                roomNo.toInt() <= 0 -> TeamRequest::roomNo.name.validateLessEqZero()
                DatabaseTransaction.validateRoomInfo(roomNo) -> TeamRequest::roomNo.name.validateNotFound()

                playerId == null -> TeamRequest::playerId.name.validateIsNullOrBlank()
                DatabaseTransaction.validatePlayer(playerId) -> TeamRequest::playerId.name.validateNotFound()

                team.isNullOrBlank() -> TeamRequest::team.name.validateIsNullOrBlank()
                !team.validateTeam() -> TeamRequest::team.name.validateIncorrect()

                else -> {
                    DatabaseTransaction.putTeam(
                        teamRequest = TeamRequest(
                            roomNo = roomNo,
                            playerId = playerId,
                            team = team
                        )
                    )
                    response.success = true
                    "Put team success"
                }
            }
            response.message = message
            call.respond(response)
        }
    }

}
