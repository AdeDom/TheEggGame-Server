package com.adedom.teg.route

import com.adedom.teg.request.PostSignUp
import com.adedom.teg.request.PutPassword
import com.adedom.teg.request.PutProfile
import com.adedom.teg.request.PutState
import com.adedom.teg.response.BaseResponse
import com.adedom.teg.response.PlayerResponse
import com.adedom.teg.response.SignUpResponse
import com.adedom.teg.transaction.DatabaseTransaction
import com.adedom.teg.util.*
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*

fun Route.getPlayer() {

    //todo JWT authentication
    route("player") {
        val playerIdKey = "player_id"
        get("/{$playerIdKey}") {
            val response = PlayerResponse()
            val playerId = call.parameters[playerIdKey]
            val message = when {
                playerId.isNullOrBlank() -> playerIdKey.validateEmpty()
                playerId.toInt() <= 0 -> playerIdKey.validateLessEqZero()
                else -> {
                    val count = DatabaseTransaction.getCountPlayer(playerId.toInt())
                    if (count == 0) {
                        playerIdKey.validateNotFound()
                    } else {
                        val player = DatabaseTransaction.getPlayer(playerId.toInt())
                        response.success = true
                        response.player = player
                        "Fetch player success"
                    }
                }
            }
            response.message = message
            call.respond(response)
        }
    }

}

fun Route.postSignUp() {

    route("sign-up") {
        post("/") {
            val response = SignUpResponse()
            val (username, password, name, gender) = call.receive<PostSignUp>()
            val minAuth = 4
            val message = when {
                username.isNullOrBlank() -> PostSignUp::username.name.validateEmpty()
                username.length < minAuth -> PostSignUp::username.name validateGrateEq minAuth
                DatabaseTransaction.getCountUsername(username) != 0 -> username.validateRepeatUsername()

                password.isNullOrBlank() -> PostSignUp::password.name.validateEmpty()
                password.length < minAuth -> PostSignUp::password.name validateGrateEq minAuth

                name.isNullOrBlank() -> PostSignUp::name.name.validateEmpty()
                DatabaseTransaction.getCountName(name) != 0 -> name.validateRepeatName()

                gender == null -> PostSignUp::gender.name.validateEmpty()
                !gender.validateGender() -> PostSignUp::gender.name.validateIncorrect()

                else -> {
                    val playerId = DatabaseTransaction.postSignUp(
                        postSignUp = PostSignUp(
                            username = username,
                            password = password,
                            name = name,
                            gender = gender
                        )
                    )
                    response.playerId = playerId
                    response.success = true
                    "Post sign up success"
                }
            }
            response.message = message
            call.respond(response)
        }
    }

}

fun Route.putPassword() {

    route("password") {
        put("/") {
            val response = BaseResponse()
            val (playerId, oldPassword, newPassword) = call.receive<PutPassword>()
            val minPassword = 4
            val message = when {
                playerId == null -> PutPassword::playerId.name.validateEmpty()
                playerId <= 0 -> PutPassword::playerId.name.validateLessEqZero()
                DatabaseTransaction.getCountPlayer(playerId) == 0 -> PutPassword::playerId.name.validateNotFound()

                oldPassword.isNullOrBlank() -> PutPassword::oldPassword.name.validateEmpty()
                DatabaseTransaction.getCountPasswordPlayer(
                    putPassword = PutPassword(
                        playerId = playerId,
                        oldPassword = oldPassword
                    )
                ) == 0 -> PutPassword::oldPassword.name.validateLessEqZero()

                newPassword.isNullOrBlank() -> PutPassword::newPassword.name.validateEmpty()
                newPassword.length < minPassword -> PutPassword::newPassword.name validateGrateEq minPassword

                else -> {
                    DatabaseTransaction.putPassword(
                        putPassword = PutPassword(
                            playerId = playerId,
                            newPassword = newPassword
                        )
                    )
                    response.success = true
                    "Put password success"
                }
            }
            response.message = message
            call.respond(response)
        }
    }

}

fun Route.putProfile() {

    //todo image profile
    route("profile") {
        put("/") {
            val response = BaseResponse()
            val (playerId, name, gender) = call.receive<PutProfile>()
            val minName = 4
            val message = when {
                playerId == null -> PutProfile::playerId.name.validateEmpty()
                playerId <= 0 -> PutProfile::playerId.name.validateLessEqZero()
                DatabaseTransaction.getCountPlayer(playerId) == 0 -> PutProfile::playerId.name.validateNotFound()

                name.isNullOrBlank() -> PutProfile::name.name.validateEmpty()
                name.length < minName -> PutProfile::name.name.validateGrateEq(minName)

                gender.isNullOrBlank() -> PutProfile::gender.name.validateEmpty()

                else -> {
                    DatabaseTransaction.putProfile(
                        putProfile = PutProfile(
                            playerId = playerId,
                            name = name,
                            gender = gender
                        )
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

fun Route.putState() {

    route("state") {
        put("/") {
            val response = BaseResponse()
            val (playerId, state) = call.receive<PutState>()
            val message = when {
                playerId == null -> PutState::playerId.name.validateEmpty()
                playerId <= 0 -> PutState::playerId.name.validateLessEqZero()
                DatabaseTransaction.getCountPlayer(playerId) == 0 -> PutState::playerId.name.validateNotFound()

                state.isNullOrBlank() -> PutState::state.name.validateEmpty()

                else -> {
                    DatabaseTransaction.putState(
                        putState = PutState(
                            playerId = playerId,
                            state = state
                        )
                    )
                    response.success = true
                    "Put state success"
                }
            }
            response.message = message
            call.respond(response)
        }
    }

}
