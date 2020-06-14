package com.adedom.teg.route

import com.adedom.teg.request.*
import com.adedom.teg.response.BaseResponse
import com.adedom.teg.response.PlayerResponse
import com.adedom.teg.response.SignInResponse
import com.adedom.teg.response.SignUpResponse
import com.adedom.teg.transaction.DatabaseTransaction
import com.adedom.teg.util.*
import com.adedom.teg.util.jwt.JwtConfig
import com.adedom.teg.util.jwt.player
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*

fun Route.getPlayer() {

    authenticate {
        route("player") {
            get("/") {
                val response = PlayerResponse()
                val playerId = call.player?.playerId
                val message = when {
                    playerId == null -> playerId.validateAccessToken()

                    else -> {
                        val player = DatabaseTransaction.getPlayer(playerId)
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

}

fun Route.postSignIn() {

    route("sign-in") {
        post("/") {
            val response = SignInResponse()
            val (username, password) = call.receive<SignInRequest>()
            val message = when {
                username.isNullOrBlank() -> SignInRequest::username.name.validateEmpty()
                username.length < CommonConstant.MIN_USERNAME -> SignInRequest::username.name validateGrateEq CommonConstant.MIN_USERNAME

                password.isNullOrBlank() -> SignInRequest::password.name.validateEmpty()
                password.length < CommonConstant.MIN_PASSWORD -> SignInRequest::password.name validateGrateEq CommonConstant.MIN_PASSWORD

                DatabaseTransaction.validateSignIn(
                    signInRequest = SignInRequest(
                        username = username,
                        password = password
                    )
                ) -> "Username and password incorrect"

                else -> {
                    val player = DatabaseTransaction.postSignIn(
                        signInRequest = SignInRequest(
                            username = username,
                            password = password
                        )
                    )
                    response.accessToken = JwtConfig.makeToken(player)
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
            val (username, password, name, gender) = call.receive<SignUpRequest>()
            val message = when {
                username.isNullOrBlank() -> SignUpRequest::username.name.validateEmpty()
                username.length < CommonConstant.MIN_USERNAME -> SignUpRequest::username.name validateGrateEq CommonConstant.MIN_USERNAME
                !DatabaseTransaction.validateUsername(username) -> username.validateRepeatUsername()

                password.isNullOrBlank() -> SignUpRequest::password.name.validateEmpty()
                password.length < CommonConstant.MIN_PASSWORD -> SignUpRequest::password.name validateGrateEq CommonConstant.MIN_PASSWORD

                name.isNullOrBlank() -> SignUpRequest::name.name.validateEmpty()
                !DatabaseTransaction.validateName(name) -> name.validateRepeatName()

                gender == null -> SignUpRequest::gender.name.validateEmpty()
                !gender.validateGender() -> SignUpRequest::gender.name.validateIncorrect()

                else -> {
                    val playerId = DatabaseTransaction.postSignUp(
                        signUpRequest = SignUpRequest(
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
            val (playerId, oldPassword, newPassword) = call.receive<PasswordRequest>()
            val message = when {
                playerId == null -> PasswordRequest::playerId.name.validateEmpty()
                playerId <= 0 -> PasswordRequest::playerId.name.validateLessEqZero()
                DatabaseTransaction.validatePlayer(playerId) -> PasswordRequest::playerId.name.validateNotFound()

                oldPassword.isNullOrBlank() -> PasswordRequest::oldPassword.name.validateEmpty()
                DatabaseTransaction.validatePasswordPlayer(
                    passwordRequest = PasswordRequest(
                        playerId = playerId,
                        oldPassword = oldPassword
                    )
                ) -> PasswordRequest::oldPassword.name.validateLessEqZero()

                newPassword.isNullOrBlank() -> PasswordRequest::newPassword.name.validateEmpty()
                newPassword.length < CommonConstant.MIN_PASSWORD -> PasswordRequest::newPassword.name validateGrateEq CommonConstant.MIN_PASSWORD

                else -> {
                    DatabaseTransaction.putPassword(
                        passwordRequest = PasswordRequest(
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
            val (playerId, name, gender) = call.receive<ProfileRequest>()
            val message = when {
                playerId == null -> ProfileRequest::playerId.name.validateEmpty()
                playerId <= 0 -> ProfileRequest::playerId.name.validateLessEqZero()
                DatabaseTransaction.validatePlayer(playerId) -> ProfileRequest::playerId.name.validateNotFound()

                name.isNullOrBlank() -> ProfileRequest::name.name.validateEmpty()
                name.length < CommonConstant.MIN_NAME -> ProfileRequest::name.name.validateGrateEq(CommonConstant.MIN_NAME)

                gender.isNullOrBlank() -> ProfileRequest::gender.name.validateEmpty()

                else -> {
                    DatabaseTransaction.putProfile(
                        profileRequest = ProfileRequest(
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
            val (playerId, state) = call.receive<StateRequest>()
            val message = when {
                playerId == null -> StateRequest::playerId.name.validateEmpty()
                playerId <= 0 -> StateRequest::playerId.name.validateLessEqZero()
                DatabaseTransaction.validatePlayer(playerId) -> StateRequest::playerId.name.validateNotFound()

                state.isNullOrBlank() -> StateRequest::state.name.validateEmpty()

                else -> {
                    DatabaseTransaction.putState(
                        stateRequest = StateRequest(
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
