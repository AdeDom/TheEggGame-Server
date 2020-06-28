package com.adedom.teg.route

import com.adedom.teg.request.SignInRequest
import com.adedom.teg.request.SignUpRequest
import com.adedom.teg.response.SignInResponse
import com.adedom.teg.service.TegService
import com.adedom.teg.util.*
import com.adedom.teg.util.jwt.JwtConfig
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.route

fun Route.authRoute(service: TegService) {

    route("sign-in") {
        post("/") {
            val response = SignInResponse()
            val (username, password) = call.receive<SignInRequest>()
            val message = when {
                username.isNullOrBlank() -> SignInRequest::username.name.validateEmpty()
                username.length < CommonConstant.MIN_USERNAME -> SignInRequest::username.name validateGrateEq CommonConstant.MIN_USERNAME

                password.isNullOrBlank() -> SignInRequest::password.name.validateEmpty()
                password.length < CommonConstant.MIN_PASSWORD -> SignInRequest::password.name validateGrateEq CommonConstant.MIN_PASSWORD

                service.validateSignIn(
                    signInRequest = SignInRequest(
                        username = username,
                        password = password
                    )
                ) -> "Username and password incorrect"

                else -> {
                    val player = service.signIn(
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

    route("sign-up") {
        post("/") {
            val response = SignInResponse()
            val (username, password, name, gender) = call.receive<SignUpRequest>()
            val message = when {
                username.isNullOrBlank() -> SignUpRequest::username.name.validateEmpty()
                username.length < CommonConstant.MIN_USERNAME -> SignUpRequest::username.name validateGrateEq CommonConstant.MIN_USERNAME
                !service.validateUsername(username) -> username.validateRepeatUsername()

                password.isNullOrBlank() -> SignUpRequest::password.name.validateEmpty()
                password.length < CommonConstant.MIN_PASSWORD -> SignUpRequest::password.name validateGrateEq CommonConstant.MIN_PASSWORD

                name.isNullOrBlank() -> SignUpRequest::name.name.validateEmpty()
                !service.validateName(name) -> name.validateRepeatName()

                gender == null -> SignUpRequest::gender.name.validateEmpty()
                !gender.validateGender() -> SignUpRequest::gender.name.validateIncorrect()

                else -> {
                    val player = service.signUp(
                        signUpRequest = SignUpRequest(
                            username = username,
                            password = password,
                            name = name,
                            gender = gender
                        )
                    )
                    response.accessToken = JwtConfig.makeToken(player)
                    response.success = true
                    "Post sign up success"
                }
            }
            response.message = message
            call.respond(response)
        }
    }

}
