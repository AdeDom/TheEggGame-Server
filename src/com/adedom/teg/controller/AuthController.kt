package com.adedom.teg.controller

import com.adedom.teg.request.SignInRequest
import com.adedom.teg.request.SignUpRequest
import com.adedom.teg.response.SignInResponse
import com.adedom.teg.service.TegService
import com.adedom.teg.util.*
import com.adedom.teg.util.jwt.JwtConfig
import com.adedom.teg.util.jwt.PlayerPrincipal
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.route

fun Route.authController(service: TegService) {

    route("auth") {
        signIn(service)
        signUp(service)
    }

}

fun Route.signIn(service: TegService) {

    route("sign-in") {
        post("/") {
            val response = SignInResponse()
            val (username, password) = call.receive<SignInRequest>()
            val message = when {
                username.isNullOrBlank() -> SignInRequest::username.name.validateEmpty()
                username.length < CommonConstant.MIN_USERNAME -> SignInRequest::username.name validateGrateEq CommonConstant.MIN_USERNAME

                password.isNullOrBlank() -> SignInRequest::password.name.validateEmpty()
                password.length < CommonConstant.MIN_PASSWORD -> SignInRequest::password.name validateGrateEq CommonConstant.MIN_PASSWORD

                else -> {
                    val pair: Pair<String, PlayerPrincipal?> = service.signIn(
                        signInRequest = SignInRequest(
                            username = username,
                            password = password
                        )
                    )
                    if (pair.second != null) {
                        response.accessToken = JwtConfig.makeToken(pair.second!!)
                        response.success = true
                    }
                    pair.first
                }
            }
            response.message = message
            call.respond(response)
        }
    }

}

fun Route.signUp(service: TegService) {

        route("sign-up") {
        post("/") {
            val response = SignInResponse()
            val (username, password, name, gender) = call.receive<SignUpRequest>()
            val message = when {
                username.isNullOrBlank() -> SignUpRequest::username.name.validateEmpty()
                username.length < CommonConstant.MIN_USERNAME -> SignUpRequest::username.name validateGrateEq CommonConstant.MIN_USERNAME

                password.isNullOrBlank() -> SignUpRequest::password.name.validateEmpty()
                password.length < CommonConstant.MIN_PASSWORD -> SignUpRequest::password.name validateGrateEq CommonConstant.MIN_PASSWORD

                name.isNullOrBlank() -> SignUpRequest::name.name.validateEmpty()

                gender == null -> SignUpRequest::gender.name.validateEmpty()
                !gender.validateGender() -> SignUpRequest::gender.name.validateIncorrect()

                else -> {
                    val pair: Pair<String, PlayerPrincipal?> = service.signUp(
                        signUpRequest = SignUpRequest(
                            username = username,
                            password = password,
                            name = name,
                            gender = gender
                        )
                    )
                    if (pair.second != null) {
                        response.accessToken = JwtConfig.makeToken(pair.second!!)
                        response.success = true
                    }
                    pair.first
                }
            }
            response.message = message
            call.respond(response)
        }
    }

}
