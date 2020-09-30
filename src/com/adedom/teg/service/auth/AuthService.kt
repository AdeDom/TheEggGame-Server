package com.adedom.teg.service.auth

import com.adedom.teg.controller.auth.model.SignUpRequest
import com.adedom.teg.controller.auth.model.SignUpResponse
import com.adedom.teg.controller.auth.model.SignInRequest
import com.adedom.teg.response.SignInResponse
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
interface AuthService {

    fun signIn(signInRequest: SignInRequest): SignInResponse

    fun signUp(signUpRequest: SignUpRequest): SignUpResponse

}
