package com.adedom.teg.business.account

import com.adedom.teg.models.request.ChangeLatLngRequest
import com.adedom.teg.models.request.ChangePasswordRequest
import com.adedom.teg.models.request.ChangeProfileRequest
import com.adedom.teg.models.request.StateRequest
import com.adedom.teg.models.response.BaseResponse
import com.adedom.teg.models.response.PlayerInfoResponse
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
interface AccountService {

    fun changeImageProfile(playerId: String?, imageName: String?): BaseResponse

    fun fetchPlayerInfo(playerId: String?): PlayerInfoResponse

    fun playerState(playerId: String?, stateRequest: StateRequest): BaseResponse

    fun changePassword(playerId: String?, changePasswordRequest: ChangePasswordRequest): BaseResponse

    fun changeProfile(playerId: String?, changeProfileRequest: ChangeProfileRequest): BaseResponse

    fun changeLatLng(playerId: String?, changeLatLngRequest: ChangeLatLngRequest): BaseResponse

}
