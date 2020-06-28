package com.adedom.teg.service

import com.adedom.teg.repositories.TegRepository
import com.adedom.teg.request.SignInRequest
import com.adedom.teg.request.SignUpRequest
import com.adedom.teg.util.jwt.PlayerPrincipal
import io.ktor.http.content.MultiPartData

class TegServiceImpl(private val repository: TegRepository) : TegService {

    override fun signIn(signInRequest: SignInRequest): PlayerPrincipal {
        return repository.postSignIn(signInRequest)
    }

    override fun validateSignIn(signInRequest: SignInRequest): Boolean {
        return repository.validateSignIn(signInRequest)
    }

    override fun signUp(signUpRequest: SignUpRequest): PlayerPrincipal {
        return repository.postSignUp(signUpRequest)
    }

    override fun validateUsername(username: String): Boolean {
        return repository.validateUsername(username)
    }

    override fun validateName(name: String): Boolean {
        return repository.validateName(name)
    }

    override suspend fun changeImageProfile(playerId: Int, multiPartData: MultiPartData) {
        repository.changeImageProfile(playerId, multiPartData)
    }

}
