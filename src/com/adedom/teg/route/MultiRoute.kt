package com.adedom.teg.route

import com.adedom.teg.request.SetLatlng
import com.adedom.teg.request.SetReady
import com.adedom.teg.request.SetRoomOff
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
import io.ktor.routing.put
import io.ktor.routing.route

fun Route.multi() {

    val response = BaseResponse()

    route("set-latlng") {
        put("/") {
            val (roomNo, playerId, latitude, longitude) = call.receive<SetLatlng>()
            val message = when {
                roomNo.isNullOrBlank() -> SetLatlng::roomNo.name.validateEmpty()
                roomNo.toInt() <= 0 -> SetLatlng::roomNo.name.validateLessEqZero()
                DatabaseTransaction.getCountRoomInfo(roomNo) == 0 -> SetLatlng::roomNo.name.validateNotFound()

                playerId == null -> SetLatlng::playerId.name.validateEmpty()
                playerId <= 0 -> SetLatlng::playerId.name.validateLessEqZero()
                DatabaseTransaction.getCountPlayer(playerId) == 0 -> SetLatlng::playerId.name.validateNotFound()

                latitude.isNull() -> SetLatlng::latitude.name.validateEmpty()

                longitude.isNull() -> SetLatlng::longitude.name.validateEmpty()

                else -> {
                    DatabaseTransaction.putSetLatLng(
                        setLatlng = SetLatlng(
                            roomNo = roomNo,
                            playerId = playerId,
                            latitude = latitude,
                            longitude = longitude
                        )
                    )
                    response.success = true
                    "Set latlng success"
                }
            }
            response.message = message
            call.respond(response)
        }
    }

    route("set-ready") {
        put("/") {
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
                    DatabaseTransaction.putSetReady(
                        setReady = SetReady(
                            roomNo = roomNo,
                            playerId = playerId,
                            status = status
                        )
                    )
                    response.success = true
                    "Set ready success"
                }
            }
            response.message = message
            call.respond(response)
        }
    }

    route("set-room-off") {
        put("/") {
            val (roomNo) = call.receive<SetRoomOff>()
            val message = when {
                roomNo.isNullOrBlank() -> SetRoomOff::roomNo.name.validateEmpty()
                roomNo.toInt() <= 0 -> SetRoomOff::roomNo.name.validateLessEqZero()
                DatabaseTransaction.getCountRoom(roomNo) == 0 -> SetRoomOff::roomNo.name.validateNotFound()
                else -> {
                    DatabaseTransaction.putSetRoomOff(roomNo)
                    response.success = true
                    "Set room off success"
                }
            }
            response.message = message
            call.respond(response)
        }
    }

}
