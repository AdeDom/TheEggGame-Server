package com.adedom.teg.route

import com.adedom.teg.request.*
import com.adedom.teg.response.BaseResponse
import com.adedom.teg.transaction.DatabaseTransaction
import com.adedom.teg.util.isNull
import com.adedom.teg.util.validateEmpty
import com.adedom.teg.util.validateLessEqZero
import com.adedom.teg.util.validateNotFound
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.delete
import io.ktor.routing.put
import io.ktor.routing.route

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

                latitude.isNull() -> PutLatlng::latitude.name.validateEmpty()

                longitude.isNull() -> PutLatlng::longitude.name.validateEmpty()

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
            val (roomNo, playerId, status) = call.receive<SetReady>()
            val message = when {
                roomNo.isNullOrBlank() -> SetReady::roomNo.name.validateEmpty()
                roomNo.toInt() <= 0 -> SetReady::roomNo.name.validateLessEqZero()
                DatabaseTransaction.getCountRoomInfo(roomNo) == 0 -> SetReady::roomNo.name.validateNotFound()

                playerId == null -> SetReady::playerId.name.validateEmpty()
                playerId <= 0 -> SetReady::playerId.name.validateLessEqZero()
                DatabaseTransaction.getCountPlayer(playerId) == 0 -> SetReady::playerId.name.validateNotFound()

                status.isNullOrBlank() -> SetReady::status.name.validateEmpty()

                else -> {
                    DatabaseTransaction.putReady(
                        setReady = SetReady(
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
            val (roomNo) = call.receive<SetRoomOff>()
            val message = when {
                roomNo.isNullOrBlank() -> SetRoomOff::roomNo.name.validateEmpty()
                roomNo.toInt() <= 0 -> SetRoomOff::roomNo.name.validateLessEqZero()
                DatabaseTransaction.getCountRoom(roomNo) == 0 -> SetRoomOff::roomNo.name.validateNotFound()
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
            val (roomNo, playerId, team) = call.receive<SetTeam>()
            val message = when {
                roomNo.isNullOrBlank() -> SetTeam::roomNo.name.validateEmpty()
                roomNo.toInt() <= 0 -> SetTeam::roomNo.name.validateLessEqZero()
                DatabaseTransaction.getCountRoomInfo(roomNo) == 0 -> SetTeam::roomNo.name.validateNotFound()

                playerId == null -> SetTeam::playerId.name.validateEmpty()
                playerId <= 0 -> SetTeam::playerId.name.validateLessEqZero()
                DatabaseTransaction.getCountPlayer(playerId) == 0 -> SetTeam::playerId.name.validateNotFound()

                team.isNullOrBlank() -> SetTeam::team.name.validateEmpty()

                else -> {
                    DatabaseTransaction.putTeam(
                        setTeam = SetTeam(
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
