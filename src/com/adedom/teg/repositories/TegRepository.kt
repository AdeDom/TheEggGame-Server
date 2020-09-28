package com.adedom.teg.repositories

import com.adedom.teg.controller.auth.model.SignUpRequest
import com.adedom.teg.controller.auth.model.SignUpResponse
import com.adedom.teg.request.account.ChangePasswordRequest
import com.adedom.teg.request.account.ChangeProfileRequest
import com.adedom.teg.request.account.StateRequest
import com.adedom.teg.request.application.LogActiveRequest
import com.adedom.teg.request.application.RankPlayersRequest
import com.adedom.teg.request.auth.SignInRequest
import com.adedom.teg.request.single.ItemCollectionRequest
import com.adedom.teg.response.*
import io.ktor.http.content.*

interface TegRepository {

    // if repeat return ture
    fun isUsernameRepeat(username: String): Boolean

    // if repeat return ture
    fun isNameRepeat(name: String): Boolean

    fun postSignIn(signInRequest: SignInRequest): SignInResponse

    fun signUp(signUpRequest: SignUpRequest): SignUpResponse

    suspend fun changeImageProfile(playerId: String, multiPartData: MultiPartData): BaseResponse

    fun fetchPlayerInfo(playerId: String): PlayerResponse

    fun playerState(playerId: String, stateRequest: StateRequest): BaseResponse

    fun changePassword(playerId: String, changePasswordRequest: ChangePasswordRequest): BaseResponse

    fun changeProfile(playerId: String, changeProfileRequest: ChangeProfileRequest): BaseResponse

    fun fetchRankPlayers(rankPlayersRequest: RankPlayersRequest): RankPlayersResponse

    fun postLogActive(playerId: String, logActiveRequest: LogActiveRequest): BaseResponse

    fun fetchItemCollection(playerId: String): BackpackResponse

    fun postItemCollection(playerId: String, itemCollectionRequest: ItemCollectionRequest): BaseResponse

}
