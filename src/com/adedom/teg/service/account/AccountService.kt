package com.adedom.teg.service.account

import com.adedom.teg.controller.account.model.ChangePasswordRequest
import com.adedom.teg.controller.account.model.ChangeProfileRequest
import com.adedom.teg.controller.account.model.StateRequest
import com.adedom.teg.response.BaseResponse
import com.adedom.teg.response.PlayerResponse
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
interface AccountService {

    fun changeImageProfile(playerId: String?, imageName: String?): BaseResponse

    fun fetchPlayerInfo(playerId: String?): PlayerResponse

    fun playerState(playerId: String?, stateRequest: StateRequest): BaseResponse

    fun changePassword(playerId: String?, changePasswordRequest: ChangePasswordRequest): BaseResponse

    fun changeProfile(playerId: String?, changeProfileRequest: ChangeProfileRequest): BaseResponse

}
