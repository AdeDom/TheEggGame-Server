package com.adedom.teg.service

import com.adedom.teg.request.SignInRequest
import com.adedom.teg.request.SignUpRequest
import com.adedom.teg.util.jwt.PlayerPrincipal
import io.ktor.http.content.MultiPartData

interface TegService {

    fun signIn(signInRequest: SignInRequest): PlayerPrincipal

    fun validateSignIn(signInRequest: SignInRequest): Boolean

    fun signUp(signUpRequest: SignUpRequest): PlayerPrincipal

    fun validateUsername(username: String): Boolean

    fun validateName(name: String): Boolean

    suspend fun changeImageProfile(playerId: Int, multiPartData: MultiPartData)

}
