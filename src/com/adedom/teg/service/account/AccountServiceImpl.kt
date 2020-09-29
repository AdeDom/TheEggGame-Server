package com.adedom.teg.service.account

import com.adedom.teg.controller.account.model.StateRequest
import com.adedom.teg.repositories.TegRepository
import com.adedom.teg.request.account.ChangePasswordRequest
import com.adedom.teg.request.account.ChangeProfileRequest
import com.adedom.teg.response.BaseResponse
import com.adedom.teg.response.PlayerResponse
import com.adedom.teg.util.toMessageIsNullOrBlank
import com.adedom.teg.util.validateAccessToken
import com.adedom.teg.util.validateIncorrect
import com.adedom.teg.util.validateState
import io.ktor.http.content.*

class AccountServiceImpl(private val repository: TegRepository) : AccountService {

    override suspend fun changeImageProfile(playerId: String, multiPartData: MultiPartData): BaseResponse {
        return BaseResponse()
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
            !state.validateState() -> StateRequest::state.name.validateIncorrect()

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

    override fun changePassword(playerId: String, changePasswordRequest: ChangePasswordRequest): BaseResponse {
        return BaseResponse()
    }

    override fun changeProfile(playerId: String, changeProfileRequest: ChangeProfileRequest): BaseResponse {
        return BaseResponse()
    }

}
