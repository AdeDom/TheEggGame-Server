package com.adedom.teg.service

import com.adedom.teg.repositories.AuthRepository
import com.adedom.teg.request.SignInRequest
import com.adedom.teg.request.SignUpRequest
import com.adedom.teg.util.jwt.PlayerPrincipal

class AuthServiceImpl(private val repository: AuthRepository) : AuthService {

    override fun signIn(signInRequest: SignInRequest): PlayerPrincipal {
        return repository.postSignIn(signInRequest)
    }

    override fun signUp(signUpRequest: SignUpRequest): PlayerPrincipal {
        return repository.postSignUp(signUpRequest)
    }

}
