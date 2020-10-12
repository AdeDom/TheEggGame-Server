package com.adedom.teg.business.auth

import com.adedom.teg.business.business.TegBusiness
import com.adedom.teg.business.jwtconfig.JwtConfig
import com.adedom.teg.data.models.SignUpDb
import com.adedom.teg.data.repositories.TegRepository
import com.adedom.teg.models.request.RefreshTokenRequest
import com.adedom.teg.models.request.SignInRequest
import com.adedom.teg.models.request.SignUpRequest
import com.adedom.teg.models.response.SignInResponse
import com.adedom.teg.models.response.Token
import com.adedom.teg.util.TegConstant
import io.ktor.http.*
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
            business.isValidateMinUsername(username) ->
                business.toMessageGrateEq(signInRequest::username, TegConstant.MIN_USERNAME)
            business.isValidateMinPassword(password) ->
                business.toMessageGrateEq(signInRequest::password, TegConstant.MIN_PASSWORD)

            // validate database
            repository.isValidateSignIn(signInRequest.copy(password = business.encryptSHA(password))) ->
                business.toMessageIncorrect(signInRequest::password)

            // execute
            else -> {
                val db = repository.signIn(signInRequest.copy(password = business.encryptSHA(password)))
                val token = Token(
                    accessToken = jwtConfig.makeAccessToken(db.playerId),
                    refreshToken = jwtConfig.makeRefreshToken(db.playerId),
                )
                response.token = token
                response.success = true
                "Sign in success"
            }
        }

        response.message = message
        return response
    }

    override fun signUp(signUpRequest: SignUpRequest): SignInResponse {
        val response = SignInResponse()
        val (username, password, name, gender, birthDate) = signUpRequest

        val message: String = when {
            // validate Null Or Blank
            username.isNullOrBlank() -> business.toMessageIsNullOrBlank(signUpRequest::username)
            password.isNullOrBlank() -> business.toMessageIsNullOrBlank(signUpRequest::password)
            name.isNullOrBlank() -> business.toMessageIsNullOrBlank(signUpRequest::name)
            gender.isNullOrBlank() -> business.toMessageIsNullOrBlank(signUpRequest::gender)
            birthDate.isNullOrBlank() -> business.toMessageIsNullOrBlank(signUpRequest::birthDate)

            // validate values of variable
            business.isValidateMinUsername(username) ->
                business.toMessageGrateEq(signUpRequest::username, TegConstant.MIN_USERNAME)
            business.isValidateMinPassword(password) ->
                business.toMessageGrateEq(signUpRequest::password, TegConstant.MIN_PASSWORD)
            !business.isValidateGender(gender) -> business.toMessageGender(signUpRequest::gender)
            business.isValidateDateTime(birthDate) -> business.toMessageIncorrect(signUpRequest::birthDate)

            // validate database
            repository.isUsernameRepeat(username) -> business.toMessageRepeat(signUpRequest::username, username)
            repository.isNameRepeat(name) -> business.toMessageRepeat(signUpRequest::name, name)

            // execute
            else -> {
                val signUp = SignUpDb(
                    username = username,
                    password = business.encryptSHA(password),
                    name = name,
                    gender = gender,
                    birthDate = business.convertBirthDateStringToLong(birthDate),
                )
                val pair = repository.signUp(signUp)
                response.success = pair.first
                val token = Token(
                    accessToken = jwtConfig.makeAccessToken(pair.second),
                    refreshToken = jwtConfig.makeRefreshToken(pair.second),
                )
                response.token = token
                "Sign up success"
            }
        }

        response.message = message
        return response
    }

    override fun refreshToken(refreshTokenRequest: RefreshTokenRequest): Pair<HttpStatusCode, SignInResponse> {
        var httpStatusCode = HttpStatusCode.OK
        val response = SignInResponse()
        val (refreshToken) = refreshTokenRequest

        val message: String = when {
            // validate Null Or Blank
            refreshToken.isNullOrBlank() -> business.toMessageIsNullOrBlank(refreshTokenRequest::refreshToken)

            // validate values of variable
            business.isValidateJwtIncorrect(refreshToken, jwtConfig.playerId) ->
                business.toMessageIncorrect(refreshTokenRequest::refreshToken)
            business.isValidateJwtExpires(refreshToken) -> {
                httpStatusCode = HttpStatusCode.Unauthorized
                business.toMessageIncorrect(refreshTokenRequest::refreshToken)
            }

            // validate database

            // execute
            else -> {
                val playerId = jwtConfig.decodeJwtGetPlayerId(refreshToken)
                val token = Token(
                    accessToken = jwtConfig.makeAccessToken(playerId),
                    refreshToken = jwtConfig.makeRefreshToken(playerId),
                )
                response.token = token
                response.success = true
                "Refresh token success"
            }
        }

        response.message = message
        return Pair(httpStatusCode, response)
    }

}
