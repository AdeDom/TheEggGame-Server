package com.adedom.teg.business.service.account

import com.adedom.teg.http.models.request.ChangePasswordRequest
import com.adedom.teg.http.models.request.ChangeProfileRequest
import com.adedom.teg.http.models.request.StateRequest
import com.adedom.teg.http.models.response.BaseResponse
import com.adedom.teg.http.models.response.PlayerResponse
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
interface AccountService {

    fun changeImageProfile(playerId: String?, imageName: String?): BaseResponse

    fun fetchPlayerInfo(playerId: String?): PlayerResponse

    fun playerState(playerId: String?, stateRequest: StateRequest): BaseResponse

    fun changePassword(playerId: String?, changePasswordRequest: ChangePasswordRequest): BaseResponse

    fun changeProfile(playerId: String?, changeProfileRequest: ChangeProfileRequest): BaseResponse

}
