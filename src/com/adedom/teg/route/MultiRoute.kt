package com.adedom.teg.route

import com.adedom.teg.request.*
import com.adedom.teg.response.*
import com.adedom.teg.transaction.DatabaseTransaction
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
                roomNo.isNullOrBlank() -> GetConstant.ROOM_NO.validateEmpty()
                roomNo.toInt() <= 0 -> GetConstant.ROOM_NO.validateLessEqZero()
                DatabaseTransaction.getCountRoom(roomNo) == 0 -> GetConstant.ROOM_NO.validateIncorrect()

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
                    response.room = listRoom
                    response.success = true
                    "Fetch rooms success"
                }
            }
            response.message = message
            call.respond(response)
        }

        post("/") {
            val response = RoomResponse()
            val (name, people, playerId) = call.receive<PostRoom>()
            val message = when {
                name.isNullOrBlank() -> PostRoom::name.name.validateEmpty()

                people.isNullOrBlank() -> PostRoom::people.name.validateEmpty()
                people.toInt() < 2 || people.toInt() > 6 -> PostRoom::people.name.validateIncorrect()

                playerId == null -> PostRoom::playerId.name.validateEmpty()
                playerId <= 0 -> PostRoom::playerId.name.validateLessEqZero()
                DatabaseTransaction.getCountPlayer(playerId) == 0 -> PostRoom::playerId.name.validateNotFound()

                else -> {
                    val roomNo = DatabaseTransaction.postRoom(
                        postRoom = PostRoom(
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
                roomNo.isNullOrBlank() -> GetConstant.ROOM_NO.validateEmpty()
                roomNo.toInt() <= 0 -> GetConstant.ROOM_NO.validateLessEqZero()
                DatabaseTransaction.getCountRoom(roomNo) == 0 -> GetConstant.ROOM_NO.validateIncorrect()

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
            val (roomNo, playerId) = call.receive<PostRoomInfo>()
            val message = when {
                roomNo.isNullOrBlank() -> PostRoomInfo::roomNo.name.validateEmpty()
                roomNo.toInt() <= 0 -> PostRoomInfo::roomNo.name.validateLessEqZero()
                DatabaseTransaction.getCountRoomInfo(roomNo) == 0 -> PostRoomInfo::roomNo.name.validateNotFound()
                DatabaseTransaction.getCountPeopleRoom(roomNo) -> "People in room is maximum"

                playerId == null -> PostRoomInfo::playerId.name.validateEmpty()
                playerId <= 0 -> PostRoomInfo::playerId.name.validateLessEqZero()
                DatabaseTransaction.getCountPlayer(playerId) == 0 -> PostRoomInfo::playerId.name.validateNotFound()

                else -> {
                    DatabaseTransaction.postRoomInfo(
                        postRoomInfo = PostRoomInfo(
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
    }

}

fun Route.multi() {

    route("multi") {
        get("fetch-multis{${GetConstant.ROOM_NO}}") {
            val response = MultisResponse()
            val roomNo = call.parameters[GetConstant.ROOM_NO]
            val message = when {
                roomNo.isNullOrBlank() -> GetConstant.ROOM_NO.validateEmpty()
                roomNo.toInt() <= 0 -> GetConstant.ROOM_NO.validateIncorrect()
                DatabaseTransaction.getCountMultiRoomNo(roomNo) -> GetConstant.ROOM_NO.validateNotFound()

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
            val (roomNo, latitude, longitude) = call.receive<PostMulti>()
            val message = when {
                roomNo.isNullOrBlank() -> PostMulti::roomNo.name.validateEmpty()
                roomNo.toInt() <= 0 -> PostMulti::roomNo.name.validateLessEqZero()
                DatabaseTransaction.getCountRoomInfo(roomNo) == 0 -> PostMulti::roomNo.name.validateNotFound()

                latitude == null -> PostMulti::latitude.name.validateEmpty()

                longitude == null -> PostMulti::longitude.name.validateEmpty()

                else -> {
                    DatabaseTransaction.postMulti(
                        postMulti = PostMulti(
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
            val (multiId, roomNo, playerId, team, latitude, longitude) = call.receive<PostMultiCollection>()
            val message = when {
                multiId == null -> PostMultiCollection::multiId.name.validateEmpty()
                multiId <= 0 -> PostMultiCollection::multiId.name.validateLessEqZero()
                DatabaseTransaction.getCountMulti(multiId) == 0 -> PostMultiCollection::multiId.name.validateNotFound()

                roomNo.isNullOrBlank() -> PostMultiCollection::roomNo.name.validateEmpty()
                roomNo.toInt() <= 0 -> PostMultiCollection::roomNo.name.validateLessEqZero()
                DatabaseTransaction.getCountRoomInfo(roomNo) == 0 -> PostMultiCollection::roomNo.name.validateNotFound()

                playerId == null -> PostMultiCollection::playerId.name.validateEmpty()
                playerId <= 0 -> PostMultiCollection::playerId.name.validateLessEqZero()
                DatabaseTransaction.getCountPlayer(playerId) == 0 -> PostMultiCollection::playerId.name.validateNotFound()

                team.isNullOrBlank() -> PostMultiCollection::team.name.validateEmpty()
                !team.validateTeam() -> PostMultiCollection::team.name.validateIncorrect()

                latitude == null -> PostMultiCollection::latitude.name.validateEmpty()

                longitude == null -> PostMultiCollection::longitude.name.validateEmpty()

                else -> {
                    DatabaseTransaction.postMultiCollection(
                        postMultiCollection = PostMultiCollection(
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
            val (roomNo, playerId, latitude, longitude) = call.receive<PutLatlng>()
            val message = when {
                roomNo.isNullOrBlank() -> PutLatlng::roomNo.name.validateEmpty()
                roomNo.toInt() <= 0 -> PutLatlng::roomNo.name.validateLessEqZero()
                DatabaseTransaction.getCountRoomInfo(roomNo) == 0 -> PutLatlng::roomNo.name.validateNotFound()

                playerId == null -> PutLatlng::playerId.name.validateEmpty()
                playerId <= 0 -> PutLatlng::playerId.name.validateLessEqZero()
                DatabaseTransaction.getCountPlayer(playerId) == 0 -> PutLatlng::playerId.name.validateNotFound()

                latitude == null -> PutLatlng::latitude.name.validateEmpty()

                longitude == null -> PutLatlng::longitude.name.validateEmpty()

                else -> {
                    DatabaseTransaction.putLatLng(
                        putLatlng = PutLatlng(
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
            val (roomNo, playerId, status) = call.receive<PutReady>()
            val message = when {
                roomNo.isNullOrBlank() -> PutReady::roomNo.name.validateEmpty()
                roomNo.toInt() <= 0 -> PutReady::roomNo.name.validateLessEqZero()
                DatabaseTransaction.getCountRoomInfo(roomNo) == 0 -> PutReady::roomNo.name.validateNotFound()

                playerId == null -> PutReady::playerId.name.validateEmpty()
                playerId <= 0 -> PutReady::playerId.name.validateLessEqZero()
                DatabaseTransaction.getCountPlayer(playerId) == 0 -> PutReady::playerId.name.validateNotFound()

                status.isNullOrBlank() -> PutReady::status.name.validateEmpty()

                else -> {
                    DatabaseTransaction.putReady(
                        putReady = PutReady(
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
            val (roomNo) = call.receive<PutRoomOff>()
            val message = when {
                roomNo.isNullOrBlank() -> PutRoomOff::roomNo.name.validateEmpty()
                roomNo.toInt() <= 0 -> PutRoomOff::roomNo.name.validateLessEqZero()
                DatabaseTransaction.getCountRoom(roomNo) == 0 -> PutRoomOff::roomNo.name.validateNotFound()
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
            val (roomNo, playerId, team) = call.receive<PutTeam>()
            val message = when {
                roomNo.isNullOrBlank() -> PutTeam::roomNo.name.validateEmpty()
                roomNo.toInt() <= 0 -> PutTeam::roomNo.name.validateLessEqZero()
                DatabaseTransaction.getCountRoomInfo(roomNo) == 0 -> PutTeam::roomNo.name.validateNotFound()

                playerId == null -> PutTeam::playerId.name.validateEmpty()
                playerId <= 0 -> PutTeam::playerId.name.validateLessEqZero()
                DatabaseTransaction.getCountPlayer(playerId) == 0 -> PutTeam::playerId.name.validateNotFound()

                team.isNullOrBlank() -> PutTeam::team.name.validateEmpty()
                !team.validateTeam() -> PutTeam::team.name.validateIncorrect()

                else -> {
                    DatabaseTransaction.putTeam(
                        putTeam = PutTeam(
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

fun Route.deletePlayerRoomInfo() {

    route("player-room-info") {
        delete("/") {
            val response = BaseResponse()
            val (roomNo, playerId) = call.receive<DeletePlayerRoomInfo>()
            val message = when {
                roomNo.isNullOrBlank() -> DeletePlayerRoomInfo::roomNo.name.validateEmpty()
                roomNo.toInt() <= 0 -> DeletePlayerRoomInfo::roomNo.name.validateLessEqZero()
                DatabaseTransaction.getCountRoomInfo(roomNo) == 0 -> DeletePlayerRoomInfo::roomNo.name.validateNotFound()

                playerId == null -> DeletePlayerRoomInfo::playerId.name.validateEmpty()
                playerId <= 0 -> DeletePlayerRoomInfo::playerId.name.validateLessEqZero()
                DatabaseTransaction.getCountPlayer(playerId) == 0 -> DeletePlayerRoomInfo::playerId.name.validateNotFound()

                else -> {
                    DatabaseTransaction.deletePlayerRoomInfo(
                        deletePlayerRoomInfo = DeletePlayerRoomInfo(
                            roomNo = roomNo,
                            playerId = playerId
                        )
                    )
                    response.success = true
                    "Delete player room info success"
                }
            }
            response.message = message
            call.respond(response)
        }
    }

}
