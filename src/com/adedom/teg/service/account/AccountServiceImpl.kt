package com.adedom.teg.service.account

import com.adedom.teg.controller.account.model.ChangePasswordRequest
import com.adedom.teg.controller.account.model.ChangeProfileRequest
import com.adedom.teg.controller.account.model.StateRequest
import com.adedom.teg.repositories.TegRepository
import com.adedom.teg.response.BaseResponse
import com.adedom.teg.response.PlayerResponse
import com.adedom.teg.route.GetConstant
import com.adedom.teg.util.*
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
class AccountServiceImpl(private val repository: TegRepository) : AccountService {

    override fun changeImageProfile(playerId: String?, imageName: String?): BaseResponse {
        val response = BaseResponse()

        val message: String? = when {
            // validate Null Or Blank
            playerId.isNullOrBlank() -> playerId.validateAccessToken()
            imageName.isNullOrBlank() -> "Not found image file [${GetConstant.IMAGE_FILE}]"

            // validate values of variable

            // validate database

            // execute
            else -> {
                response.success = repository.changeImageProfile(playerId, imageName)
                "Put change image profile success"
            }
        }

        response.message = message
        return response
    }

    override fun fetchPlayerInfo(playerId: String?): PlayerResponse {
        val response = PlayerResponse()

        val message: String = when {
            // validate Null Or Blank
            playerId.isNullOrBlank() -> playerId.validateAccessToken()

            // validate values of variable

            // validate database

            // execute
            else -> {
                response.success = true
                response.playerInfo = repository.fetchPlayerInfo(playerId)
                "Fetch player info success"
            }
        }

        response.message = message
        return response
    }

    override fun playerState(playerId: String?, stateRequest: StateRequest): BaseResponse {
        val response = BaseResponse()
        val (state) = stateRequest
        val message: String = when {
            // validate Null Or Blank
            playerId.isNullOrBlank() -> playerId.validateAccessToken()
            state.isNullOrBlank() -> stateRequest::state.name.toMessageIsNullOrBlank()

            // validate values of variable
            !state.validateState() -> stateRequest::state.name.validateIncorrect()

            // validate database

            // execute
            else -> {
                response.success = repository.playerState(playerId, stateRequest)
                "Patch state success"
            }
        }

        response.message = message
        return response
    }

    override fun changePassword(playerId: String?, changePasswordRequest: ChangePasswordRequest): BaseResponse {
        val response = BaseResponse()
        val (oldPassword, newPassword) = changePasswordRequest
        val message: String = when {
            // validate Null Or Blank
            playerId.isNullOrBlank() -> playerId.validateAccessToken()
            oldPassword.isNullOrBlank() -> changePasswordRequest::oldPassword.name.toMessageIsNullOrBlank()
            newPassword.isNullOrBlank() -> changePasswordRequest::newPassword.name.toMessageIsNullOrBlank()

            // validate values of variable
            oldPassword.length < TegConstant.MIN_PASSWORD ->
                changePasswordRequest::oldPassword.name.validateGrateEq(TegConstant.MIN_PASSWORD)
            newPassword.length < TegConstant.MIN_PASSWORD ->
                changePasswordRequest::newPassword.name.validateGrateEq(TegConstant.MIN_PASSWORD)

            // validate database
            repository.isValidateChangePassword(playerId, changePasswordRequest) -> "Password incorrect"

            // execute
            else -> {
                response.success = repository.changePassword(playerId, changePasswordRequest)
                "Put change password success"
            }
        }

        response.message = message
        return response
    }

    override fun changeProfile(playerId: String?, changeProfileRequest: ChangeProfileRequest): BaseResponse {
        val response = BaseResponse()
        val (name, gender, birthdate) = changeProfileRequest
        val message: String = when {
            // validate Null Or Blank
            playerId.isNullOrBlank() -> playerId.validateAccessToken()
            name.isNullOrBlank() -> changeProfileRequest::name.name.toMessageIsNullOrBlank()
            gender.isNullOrBlank() -> changeProfileRequest::gender.name.toMessageIsNullOrBlank()
            birthdate.isNullOrBlank() -> changeProfileRequest::birthdate.name.toMessageIsNullOrBlank()

            // validate values of variable
            !gender.isValidateGender() -> changeProfileRequest::gender.name.validateIncorrect()
            birthdate.isValidateDateTime() -> changeProfileRequest::birthdate.name.toMessageIncorrect()

            // validate database
            repository.isNameRepeat(name) -> changeProfileRequest::name.name.toMessageRepeat(name)

            // execute
            else -> {
                response.success = repository.changeProfile(playerId, changeProfileRequest)
                "Put change profile success"
            }
        }

        response.message = message
        return response
    }

}
