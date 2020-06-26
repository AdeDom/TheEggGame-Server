package com.adedom.teg.service

import com.adedom.teg.request.SignInRequest
import com.adedom.teg.request.SignUpRequest
import com.adedom.teg.util.jwt.PlayerPrincipal

interface AuthService {

    fun signIn(signInRequest: SignInRequest): PlayerPrincipal

    fun validateSignIn(signInRequest: SignInRequest): Boolean

    fun signUp(signUpRequest: SignUpRequest): PlayerPrincipal

    fun validateUsername(username: String): Boolean

    fun validateName(name: String): Boolean

}
