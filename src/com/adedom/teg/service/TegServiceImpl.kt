package com.adedom.teg.service

import com.adedom.teg.repositories.TegRepository
import com.adedom.teg.request.account.ImageProfile
import com.adedom.teg.request.auth.SignInRequest
import com.adedom.teg.request.auth.SignUpRequest
import com.adedom.teg.util.jwt.PlayerPrincipal
import io.ktor.http.content.MultiPartData

class TegServiceImpl(private val repository: TegRepository) : TegService {

    override fun signIn(signInRequest: SignInRequest): Pair<String, PlayerPrincipal?> {
        return repository.postSignIn(signInRequest)
    }

    override fun signUp(signUpRequest: SignUpRequest): Pair<String, PlayerPrincipal?> {
        return repository.postSignUp(signUpRequest)
    }

    override suspend fun changeImageProfile(playerId: Int, multiPartData: MultiPartData): Pair<String, ImageProfile?> {
        return repository.changeImageProfile(playerId, multiPartData)
    }

}
