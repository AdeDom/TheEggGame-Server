package com.adedom.teg.service

import com.adedom.teg.request.account.ChangePasswordRequest
import com.adedom.teg.request.account.ChangeProfileRequest
import com.adedom.teg.request.account.StateRequest
import com.adedom.teg.request.application.LogActiveRequest
import com.adedom.teg.request.application.RankPlayersRequest
import com.adedom.teg.request.auth.SignInRequest
import com.adedom.teg.request.auth.SignUpRequest
import com.adedom.teg.request.single.ItemCollectionRequest
import com.adedom.teg.response.*
import io.ktor.http.content.MultiPartData

interface TegService {

    fun signIn(signInRequest: SignInRequest): SignInResponse

    fun signUp(signUpRequest: SignUpRequest): SignInResponse

    suspend fun changeImageProfile(playerId: Int, multiPartData: MultiPartData): BaseResponse

    fun fetchPlayerInfo(playerId: Int): PlayerResponse

    fun playerState(playerId: Int, stateRequest: StateRequest): BaseResponse

    fun changePassword(playerId: Int, changePasswordRequest: ChangePasswordRequest): BaseResponse

    fun changeProfile(playerId: Int, changeProfileRequest: ChangeProfileRequest): BaseResponse

    fun fetchRankPlayers(rankPlayersRequest: RankPlayersRequest): RankPlayersResponse

    fun postLogActive(playerId: Int, logActiveRequest: LogActiveRequest): BaseResponse

    fun fetchItemCollection(playerId: Int): BackpackResponse

    fun postItemCollection(playerId: Int, itemCollectionRequest: ItemCollectionRequest): BaseResponse

}
