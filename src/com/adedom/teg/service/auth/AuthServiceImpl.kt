package com.adedom.teg.service.auth

import com.adedom.teg.controller.auth.model.SignInRequest
import com.adedom.teg.controller.auth.model.SignUpRequest
import com.adedom.teg.controller.auth.model.SignUpResponse
import com.adedom.teg.repositories.TegRepository
import com.adedom.teg.response.SignInResponse
import com.adedom.teg.service.business.TegBusiness
import com.adedom.teg.util.TegConstant
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
class AuthServiceImpl(
    private val repository: TegRepository,
    private val business: TegBusiness,
) : AuthService {

    override fun signIn(signInRequest: SignInRequest): SignInResponse {
        val response = SignInResponse()

        val (username, password) = signInRequest
        val message: String = when {
            // validate Null Or Blank
            username.isNullOrBlank() -> business.toMessageIsNullOrBlank(signInRequest::username)
            password.isNullOrBlank() -> business.toMessageIsNullOrBlank(signInRequest::password)

            // validate values of variable
            username.length < TegConstant.MIN_USERNAME ->
                business.validateGrateEq(signInRequest::username, TegConstant.MIN_USERNAME)
            password.length < TegConstant.MIN_PASSWORD ->
                business.validateGrateEq(signInRequest::password, TegConstant.MIN_PASSWORD)

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
            username.isNullOrBlank() -> business.toMessageIsNullOrBlank(signUpRequest::username)
            password.isNullOrBlank() -> business.toMessageIsNullOrBlank(signUpRequest::password)
            name.isNullOrBlank() -> business.toMessageIsNullOrBlank(signUpRequest::name)
            gender.isNullOrBlank() -> business.toMessageIsNullOrBlank(signUpRequest::gender)
            birthdate.isNullOrBlank() -> business.toMessageIsNullOrBlank(signUpRequest::birthdate)

            // validate values of variable
            username.length < TegConstant.MIN_USERNAME ->
                business.validateGrateEq(signUpRequest::username, TegConstant.MIN_USERNAME)
            password.length < TegConstant.MIN_PASSWORD ->
                business.validateGrateEq(signUpRequest::password, TegConstant.MIN_PASSWORD)
            !business.isValidateGender(gender) -> business.toMessageGender(signUpRequest::gender)
            business.isValidateDateTime(birthdate) -> business.toMessageIncorrect(signUpRequest::birthdate)

            // validate database
            repository.isUsernameRepeat(username) -> business.toMessageRepeat(signUpRequest::username, username)
            repository.isNameRepeat(name) -> business.toMessageRepeat(signUpRequest::name, name)

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
