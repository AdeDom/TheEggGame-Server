package com.adedom.teg.repositories

import com.adedom.teg.request.SignInRequest
import com.adedom.teg.request.SignUpRequest
import com.adedom.teg.util.jwt.PlayerPrincipal
import io.ktor.http.content.MultiPartData

interface TegRepository {

    fun postSignIn(signInRequest: SignInRequest): Pair<String, PlayerPrincipal?>

    fun postSignUp(signUpRequest: SignUpRequest): Pair<String, PlayerPrincipal?>

    suspend fun changeImageProfile(playerId: Int, multiPartData: MultiPartData)

}
