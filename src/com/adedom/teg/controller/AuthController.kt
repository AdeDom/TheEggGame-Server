package com.adedom.teg.controller

import com.adedom.teg.request.auth.SignInRequest
import com.adedom.teg.request.auth.SignUpRequest
import com.adedom.teg.response.SignInResponse
import com.adedom.teg.service.TegService
import com.adedom.teg.util.*
import io.ktor.application.call
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route

fun Route.authController(service: TegService) {

    post<SignInRequest> { request ->
        val response = SignInResponse()
        val (username, password) = call.receive<SignInRequest>()
        val message = when {
            username.isNullOrBlank() -> request::username.name.validateEmpty()
            username.length < CommonConstant.MIN_USERNAME -> request::username.name validateGrateEq CommonConstant.MIN_USERNAME

            password.isNullOrBlank() -> request::password.name.validateEmpty()
            password.length < CommonConstant.MIN_PASSWORD -> request::password.name validateGrateEq CommonConstant.MIN_PASSWORD

            else -> {
                val service: SignInResponse = service.signIn(SignInRequest(username, password))
                response.success = service.success
                response.accessToken = service.accessToken
                service.message
            }
        }
        response.message = message
        call.respond(response)
    }

    post<SignUpRequest> { request ->
        val response = SignInResponse()
        val (username, password, name, gender) = call.receive<SignUpRequest>()
        val message = when {
            username.isNullOrBlank() -> request::username.name.validateEmpty()
            username.length < CommonConstant.MIN_USERNAME -> request::username.name validateGrateEq CommonConstant.MIN_USERNAME

            password.isNullOrBlank() -> request::password.name.validateEmpty()
            password.length < CommonConstant.MIN_PASSWORD -> request::password.name validateGrateEq CommonConstant.MIN_PASSWORD

            name.isNullOrBlank() -> request::name.name.validateEmpty()

            gender == null -> request::gender.name.validateEmpty()
            !gender.validateGender() -> request::gender.name.validateIncorrect()

            else -> {
                val service: SignInResponse = service.signUp(SignUpRequest(username, password, name, gender))
                response.success = service.success
                response.accessToken = service.accessToken
                service.message
            }
        }
        response.message = message
        call.respond(response)
    }

}
