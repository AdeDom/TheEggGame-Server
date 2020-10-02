package com.adedom.teg.business.service.auth

import com.adedom.teg.business.business.TegBusiness
import com.adedom.teg.business.jwtconfig.JwtConfig
import com.adedom.teg.business.models.SignUpItem
import com.adedom.teg.data.repositories.TegRepository
import com.adedom.teg.http.models.request.RefreshTokenRequest
import com.adedom.teg.http.models.request.SignInRequest
import com.adedom.teg.http.models.request.SignUpRequest
import com.adedom.teg.http.models.response.SignInResponse
import com.adedom.teg.http.models.response.SignUpResponse
import com.adedom.teg.util.TegConstant
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
class AuthServiceImpl(
    private val repository: TegRepository,
    private val business: TegBusiness,
    private val jwtConfig: JwtConfig,
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
                val playerId = repository.signIn(signInRequest)
                response.success = true
                response.accessToken = jwtConfig.makeAccessToken(playerId)
                response.refreshToken = jwtConfig.makeRefreshToken(playerId)
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
                val signUpItem = SignUpItem(
                    username = username,
                    password = password,
                    name = name,
                    gender = gender,
                    birthdate = business.convertBirthdateStringToLong(birthdate),
                )
                val pair = repository.signUp(signUpItem)
                response.success = pair.first
                response.accessToken = jwtConfig.makeAccessToken(pair.second)
                response.refreshToken = jwtConfig.makeRefreshToken(pair.second)
                "Sign up success"
            }
        }

        response.message = message
        return response
    }

    override fun refreshToken(refreshTokenRequest: RefreshTokenRequest): SignInResponse {
        val response = SignInResponse()
        val (refreshToken) = refreshTokenRequest

        val message: String = when {
            // validate Null Or Blank
            refreshToken.isNullOrBlank() -> business.toMessageIsNullOrBlank(refreshTokenRequest::refreshToken)

            // validate values of variable
            business.isValidateJWT(refreshToken, jwtConfig.playerId) ->
                business.toMessageIncorrect(refreshTokenRequest::refreshToken)

            // validate database

            // execute
            else -> {
                val playerId = jwtConfig.decodeJwtGetPlayerId(refreshToken)
                response.accessToken = jwtConfig.makeAccessToken(playerId)
                response.refreshToken = jwtConfig.makeRefreshToken(playerId)
                response.success = true
                "Refresh token success"
            }
        }

        response.message = message
        return response
    }

}
