package com.adedom.teg.controller.auth

import com.adedom.teg.controller.auth.model.SignUpRequest
import com.adedom.teg.request.auth.SignInRequest
import com.adedom.teg.service.auth.AuthService
import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

@KtorExperimentalLocationsAPI
fun Route.authController(service: AuthService) {

    post<SignInRequest> {
        val request = call.receive<SignInRequest>()
        val response = service.signIn(request)
        call.respond(response)
    }

    post<SignUpRequest> {
        val request = call.receive<SignUpRequest>()
        val response = service.signUp(request)
        call.respond(response)
    }

}
