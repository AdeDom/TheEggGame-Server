package com.adedom.teg.service.auth

import com.adedom.teg.controller.auth.model.SignUpRequest
import com.adedom.teg.controller.auth.model.SignUpResponse
import com.adedom.teg.repositories.TegRepository
import com.adedom.teg.request.auth.SignInRequest
import com.adedom.teg.response.SignInResponse
import com.adedom.teg.util.*

class AuthServiceImpl(private val repository: TegRepository) : AuthService {

    override fun signIn(signInRequest: SignInRequest): SignInResponse {
        val response = SignInResponse()

        val (username, password) = signInRequest
        val message: String = when {
            // validate Null Or Blank
            username.isNullOrBlank() -> signInRequest::username.name.validateIsNullOrBlank()
            password.isNullOrBlank() -> signInRequest::password.name.validateIsNullOrBlank()

            // validate values of variable
            username.length < TegConstant.MIN_USERNAME -> signInRequest::username.name.validateGrateEq(TegConstant.MIN_USERNAME)
            password.length < TegConstant.MIN_PASSWORD -> signInRequest::password.name.validateGrateEq(TegConstant.MIN_PASSWORD)

            // validate database
            repository.isValidateSignIn(signInRequest) -> "Username and password incorrect"

            // execute
            else -> {
                response.success = true
                response.accessToken = repository.signIn(signInRequest)
                "Sign in success"
            }
        }

        response.message = message
        return response
    }

    override fun signUp(signUpRequest: SignUpRequest): SignUpResponse {
        val response = SignUpResponse()
        val (username, password, name, gender, birthdate) = signUpRequest

        val message: String = when {
            // validate Null Or Blank
            username.isNullOrBlank() -> signUpRequest::username.name.validateIsNullOrBlank()
            password.isNullOrBlank() -> signUpRequest::password.name.validateIsNullOrBlank()
            name.isNullOrBlank() -> signUpRequest::name.name.validateIsNullOrBlank()
            gender.isNullOrBlank() -> signUpRequest::gender.name.validateIsNullOrBlank()
            birthdate.isNullOrBlank() -> signUpRequest::birthdate.name.validateIsNullOrBlank()

            // validate values of variable
            username.length < TegConstant.MIN_USERNAME -> signUpRequest::username.name.validateGrateEq(TegConstant.MIN_USERNAME)
            password.length < TegConstant.MIN_PASSWORD -> signUpRequest::password.name.validateGrateEq(TegConstant.MIN_PASSWORD)
            !gender.isValidateGender() -> signUpRequest::gender.name.toMessageGender()
            birthdate.isValidateDateTime() -> signUpRequest::birthdate.name.toMessageIncorrect()

            // validate database
            repository.isUsernameRepeat(username) -> signUpRequest::username.name.toMessageRepeat(username)
            repository.isNameRepeat(name) -> signUpRequest::name.name.toMessageRepeat(name)

            // execute
            else -> {
                val signUp = repository.signUp(signUpRequest)
                response.success = signUp.success
                response.accessToken = signUp.accessToken
                "Sign up success"
            }
        }

        response.message = message
        return response
    }

}
