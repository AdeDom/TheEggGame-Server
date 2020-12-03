package com.adedom.teg.refactor

import com.adedom.teg.models.request.MultiRequest
import com.adedom.teg.models.response.BaseResponse
import com.adedom.teg.models.response.MultisResponse
import com.adedom.teg.util.validateIncorrect
import com.adedom.teg.util.validateIsNullOrBlank
import com.adedom.teg.util.validateLessEqZero
import com.adedom.teg.util.validateNotFound
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

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
