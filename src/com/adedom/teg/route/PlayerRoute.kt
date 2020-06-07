package com.adedom.teg.route

import com.adedom.teg.request.*
import com.adedom.teg.response.BaseResponse
import com.adedom.teg.response.PlayerResponse
import com.adedom.teg.response.SignInResponse
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
        get("fetch-player{${GetConstant.PLAYER_ID}}") {
            val response = PlayerResponse()
            val playerId = call.parameters[GetConstant.PLAYER_ID]
            val message = when {
                playerId.isNullOrBlank() -> GetConstant.PLAYER_ID.validateEmpty()
                playerId.toInt() <= 0 -> GetConstant.PLAYER_ID.validateLessEqZero()
                DatabaseTransaction.getCountPlayer(playerId.toInt()) == 0 -> GetConstant.PLAYER_ID.validateNotFound()
                else -> {
                    val player = DatabaseTransaction.getPlayer(playerId.toInt())
                    response.success = true
                    response.player = player
                    "Fetch player success"
                }
            }
            response.message = message
            call.respond(response)
        }
    }

}

fun Route.postSignIn() {

    route("sign-in") {
        post("/") {
            val response = SignInResponse()
            val (username, password) = call.receive<PostSignIn>()
            val message = when {
                username.isNullOrBlank() -> PostSignIn::username.name.validateEmpty()
                username.length < CommonConstant.MIN_USERNAME -> PostSignIn::username.name validateGrateEq CommonConstant.MIN_USERNAME

                password.isNullOrBlank() -> PostSignIn::password.name.validateEmpty()
                password.length < CommonConstant.MIN_PASSWORD -> PostSignIn::password.name validateGrateEq CommonConstant.MIN_PASSWORD

                DatabaseTransaction.getCountSignIn(
                    postSignIn = PostSignIn(
                        username = username,
                        password = password
                    )
                ) -> "Username and password incorrect"

                else -> {
                    val playerId = DatabaseTransaction.postSignIn(
                        postSignIn = PostSignIn(
                            username = username,
                            password = password
                        )
                    )
                    response.playerId = playerId
                    response.success = true
                    "Post sign in success"
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
            val message = when {
                username.isNullOrBlank() -> PostSignUp::username.name.validateEmpty()
                username.length < CommonConstant.MIN_USERNAME -> PostSignUp::username.name validateGrateEq CommonConstant.MIN_USERNAME
                DatabaseTransaction.getCountUsername(username) != 0 -> username.validateRepeatUsername()

                password.isNullOrBlank() -> PostSignUp::password.name.validateEmpty()
                password.length < CommonConstant.MIN_PASSWORD -> PostSignUp::password.name validateGrateEq CommonConstant.MIN_PASSWORD

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
            val message = when {
                playerId == null -> PutPassword::playerId.name.validateEmpty()
                playerId <= 0 -> PutPassword::playerId.name.validateLessEqZero()
                DatabaseTransaction.getCountPlayer(playerId) == 0 -> PutPassword::playerId.name.validateNotFound()

                oldPassword.isNullOrBlank() -> PutPassword::oldPassword.name.validateEmpty()
                DatabaseTransaction.validatePasswordPlayer(
                    putPassword = PutPassword(
                        playerId = playerId,
                        oldPassword = oldPassword
                    )
                ) -> PutPassword::oldPassword.name.validateLessEqZero()

                newPassword.isNullOrBlank() -> PutPassword::newPassword.name.validateEmpty()
                newPassword.length < CommonConstant.MIN_PASSWORD -> PutPassword::newPassword.name validateGrateEq CommonConstant.MIN_PASSWORD

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
            val message = when {
                playerId == null -> PutProfile::playerId.name.validateEmpty()
                playerId <= 0 -> PutProfile::playerId.name.validateLessEqZero()
                DatabaseTransaction.getCountPlayer(playerId) == 0 -> PutProfile::playerId.name.validateNotFound()

                name.isNullOrBlank() -> PutProfile::name.name.validateEmpty()
                name.length < CommonConstant.MIN_NAME -> PutProfile::name.name.validateGrateEq(CommonConstant.MIN_NAME)

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
