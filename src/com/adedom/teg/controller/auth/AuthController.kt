package com.adedom.teg.controller.auth

import com.adedom.teg.controller.auth.model.SignUpRequest
import com.adedom.teg.request.auth.SignInRequest
import com.adedom.teg.response.SignInResponse
import com.adedom.teg.service.auth.AuthService
import com.adedom.teg.util.CommonConstant
import com.adedom.teg.util.validateGrateEq
import com.adedom.teg.util.validateIsNullOrBlank
import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

@KtorExperimentalLocationsAPI
fun Route.authController(service: AuthService) {

    post<SignInRequest> { request ->
        val response = SignInResponse()
        val (username, password) = call.receive<SignInRequest>()
        val message = when {
            username.isNullOrBlank() -> request::username.name.validateIsNullOrBlank()
            username.length < CommonConstant.MIN_USERNAME -> request::username.name validateGrateEq CommonConstant.MIN_USERNAME

            password.isNullOrBlank() -> request::password.name.validateIsNullOrBlank()
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

    post<SignUpRequest> {
        val request = call.receive<SignUpRequest>()
        val response = service.signUp(request)
        call.respond(response)
    }

}
