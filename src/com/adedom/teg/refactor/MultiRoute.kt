package com.adedom.teg.refactor

import com.adedom.teg.models.request.MultiCollectionRequest
import com.adedom.teg.models.request.MultiRequest
import com.adedom.teg.models.request.ReadyRequest
import com.adedom.teg.models.request.TeamRequest
import com.adedom.teg.models.response.BaseResponse
import com.adedom.teg.models.response.MultisResponse
import com.adedom.teg.models.response.ScoreResponse
import com.adedom.teg.util.*
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
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
