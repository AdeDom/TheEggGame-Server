package com.adedom.teg.route

import com.adedom.teg.request.SetLatlng
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

    route("set-latlng") {
        put("/") {
            val response = BaseResponse()
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

}
