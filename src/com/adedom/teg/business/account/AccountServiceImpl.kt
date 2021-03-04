package com.adedom.teg.business.account

import com.adedom.teg.business.business.TegBusiness
import com.adedom.teg.data.models.ChangeProfileDb
import com.adedom.teg.data.repositories.TegRepository
import com.adedom.teg.http.constant.ApiConstant
import com.adedom.teg.models.request.ChangeLatLngRequest
import com.adedom.teg.models.request.ChangePasswordRequest
import com.adedom.teg.models.request.ChangeProfileRequest
import com.adedom.teg.models.request.StateRequest
import com.adedom.teg.models.response.BaseResponse
import com.adedom.teg.models.response.PlayerInfo
import com.adedom.teg.models.response.PlayerInfoResponse
import com.adedom.teg.util.TegConstant
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
class AccountServiceImpl(
    private val repository: TegRepository,
    private val business: TegBusiness,
) : AccountService {

    override fun changeImageProfile(playerId: String?, imageName: String?): BaseResponse {
        val response = BaseResponse()

        val message: String = when {
            // validate Null Or Blank
            playerId.isNullOrBlank() -> business.toMessageIsNullOrBlank(playerId)
            imageName.isNullOrBlank() -> business.toMessageIsNullOrBlank(ApiConstant.IMAGE_FILE)

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

    override fun fetchPlayerInfo(playerId: String?): PlayerInfoResponse {
        val response = PlayerInfoResponse()

        val message: String = when {
            // validate Null Or Blank
            playerId.isNullOrBlank() -> business.toMessageIsNullOrBlank(playerId)

            // validate values of variable

            // validate database

            // execute
            else -> {
                val db = repository.fetchPlayerInfo(playerId)
                val playerInfo = PlayerInfo(
                    playerId = db.playerId,
                    username = db.username,
                    name = db.name.capitalize(),
                    image = db.image,
                    level = business.toConvertLevel(db.level),
                    state = db.state,
                    gender = db.gender,
                    birthDate = business.toConvertDateTimeLongToString(db.birthDate),
                    latitude = db.latitude,
                    longitude = db.longitude,
                )
                response.success = true
                response.playerInfo = playerInfo
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
            playerId.isNullOrBlank() -> business.toMessageIsNullOrBlank(playerId)
            state.isNullOrBlank() -> business.toMessageIsNullOrBlank(stateRequest::state)

            // validate values of variable
            !business.isValidateState(state) -> business.toMessageIncorrect(stateRequest::state)

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
            playerId.isNullOrBlank() -> business.toMessageIsNullOrBlank(playerId)
            oldPassword.isNullOrBlank() -> business.toMessageIsNullOrBlank(changePasswordRequest::oldPassword)
            newPassword.isNullOrBlank() -> business.toMessageIsNullOrBlank(changePasswordRequest::newPassword)

            // validate values of variable
            business.isValidateMinUsername(oldPassword) ->
                business.toMessageGrateEq(changePasswordRequest::oldPassword, TegConstant.MIN_PASSWORD)
            business.isValidateMinPassword(newPassword) ->
                business.toMessageGrateEq(changePasswordRequest::newPassword, TegConstant.MIN_PASSWORD)

            // validate database
            repository.isValidateChangePassword(
                playerId,
                changePasswordRequest.copy(oldPassword = business.encryptSHA(oldPassword))
            ) -> business.toMessageIncorrect(changePasswordRequest::oldPassword)

            // execute
            else -> {
                response.success = repository.changePassword(
                    playerId,
                    changePasswordRequest.copy(newPassword = business.encryptSHA(newPassword))
                )
                "Put change password success"
            }
        }

        response.message = message
        return response
    }

    override fun changeProfile(playerId: String?, changeProfileRequest: ChangeProfileRequest): BaseResponse {
        val response = BaseResponse()
        val (name, gender, birthDate) = changeProfileRequest
        val message: String = when {
            // validate Null Or Blank
            playerId.isNullOrBlank() -> business.toMessageIsNullOrBlank(playerId)
            name.isNullOrBlank() -> business.toMessageIsNullOrBlank(changeProfileRequest::name)
            gender.isNullOrBlank() -> business.toMessageIsNullOrBlank(changeProfileRequest::gender)
            birthDate.isNullOrBlank() -> business.toMessageIsNullOrBlank(changeProfileRequest::birthDate)

            // validate values of variable
            !business.isValidateGender(gender) -> business.toMessageIncorrect(changeProfileRequest::gender)
            business.isValidateDateTime(birthDate) -> business.toMessageIncorrect(changeProfileRequest::birthDate)

            // validate database
            repository.isNameRepeat(name) -> business.toMessageRepeat(changeProfileRequest::name, name)

            // execute
            else -> {
                val changeProfile = ChangeProfileDb(
                    name = name,
                    gender = gender,
                    birthDate = business.convertBirthDateStringToLong(birthDate),
                )
                response.success = repository.changeProfile(playerId, changeProfile)
                "Put change profile success"
            }
        }

        response.message = message
        return response
    }

    override fun changeLatLng(playerId: String?, changeLatLngRequest: ChangeLatLngRequest): BaseResponse {
        val response = BaseResponse()
        val (latitude, longitude) = changeLatLngRequest

        val message: String = when {
            // validate Null Or Blank
            playerId.isNullOrBlank() -> business.toMessageIsNullOrBlank(playerId)
            latitude == null -> business.toMessageIsNullOrBlank2(changeLatLngRequest::latitude)
            longitude == null -> business.toMessageIsNullOrBlank2(changeLatLngRequest::longitude)

            // validate values of variable

            // validate database

            // execute
            else -> {
                response.success = repository.changeLatLng(playerId, changeLatLngRequest)
                "Put change lat lng success"
            }
        }

        response.message = message
        return response
    }

}
