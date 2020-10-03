package com.adedom.teg.business.service.auth

import com.adedom.teg.http.models.request.RefreshTokenRequest
import com.adedom.teg.http.models.request.SignInRequest
import com.adedom.teg.http.models.request.SignUpRequest
import com.adedom.teg.http.models.response.SignUpResponse
import com.adedom.teg.http.models.response.SignInResponse
import io.ktor.http.*
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
interface AuthService {

    fun signIn(signInRequest: SignInRequest): SignInResponse

    fun signUp(signUpRequest: SignUpRequest): SignUpResponse

    fun refreshToken(refreshTokenRequest: RefreshTokenRequest): Pair<HttpStatusCode, SignInResponse>

}
