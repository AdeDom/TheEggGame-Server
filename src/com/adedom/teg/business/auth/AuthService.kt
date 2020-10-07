package com.adedom.teg.business.auth

import com.adedom.teg.models.request.RefreshTokenRequest
import com.adedom.teg.models.request.SignInRequest
import com.adedom.teg.models.request.SignUpRequest
import com.adedom.teg.models.response.SignInResponse
import io.ktor.http.*
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
interface AuthService {

    fun signIn(signInRequest: SignInRequest): SignInResponse

    fun signUp(signUpRequest: SignUpRequest): SignInResponse

    fun refreshToken(refreshTokenRequest: RefreshTokenRequest): Pair<HttpStatusCode, SignInResponse>

}
