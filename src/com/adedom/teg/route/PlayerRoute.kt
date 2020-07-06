package com.adedom.teg.route

import com.adedom.teg.request.ProfileRequest
import com.adedom.teg.response.BaseResponse
import com.adedom.teg.transaction.DatabaseTransaction
import com.adedom.teg.util.*
import com.adedom.teg.util.jwt.player
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.put
import io.ktor.routing.route

fun Route.putProfile() {

    route("profile") {
        put("/") {
            val response = BaseResponse()
            val (name, gender) = call.receive<ProfileRequest>()
            val playerId = call.player?.playerId
            val message = when {
                playerId == null -> playerId.validateAccessToken()

                name.isNullOrBlank() -> ProfileRequest::name.name.validateEmpty()
                name.length < CommonConstant.MIN_NAME -> ProfileRequest::name.name.validateGrateEq(CommonConstant.MIN_NAME)

                gender.isNullOrBlank() -> ProfileRequest::gender.name.validateEmpty()
                !gender.validateGender() -> ProfileRequest::gender.name.validateIncorrect()

                else -> {
                    DatabaseTransaction.putProfile(
                        playerId,
                        ProfileRequest(name = name, gender = gender)
                    )
                    response.success = true
                    "Put profile success"
                }
            }
            response.message = message
            call.respond(response)
        }
    }

}
