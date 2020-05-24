package com.adedom.teg.route

import com.adedom.teg.request.SetLatlng
import com.adedom.teg.response.BaseResponse
import com.adedom.teg.util.isNull
import com.adedom.teg.util.validateEmpty
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
                playerId.isNull() -> SetLatlng::playerId.name.validateEmpty()
                latitude.isNull() -> SetLatlng::latitude.name.validateEmpty()
                longitude.isNull() -> SetLatlng::longitude.name.validateEmpty()
                else -> {
                    response.success = true
                    "Set latlng success"
                }
            }
            response.message = message
            call.respond(response)
        }
    }

}
