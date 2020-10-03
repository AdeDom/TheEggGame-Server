package com.adedom.teg.http.controller

import com.adedom.teg.business.service.auth.AuthService
import com.adedom.teg.http.models.request.RefreshTokenRequest
import com.adedom.teg.http.models.request.SignInRequest
import com.adedom.teg.http.models.request.SignUpRequest
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

    post<RefreshTokenRequest> {
        val request = call.receive<RefreshTokenRequest>()
        val response = service.refreshToken(request)
        call.respond(response.first, response.second)
    }

}
