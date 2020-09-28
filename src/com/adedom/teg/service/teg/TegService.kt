package com.adedom.teg.service.teg

import com.adedom.teg.request.account.ChangePasswordRequest
import com.adedom.teg.request.account.ChangeProfileRequest
import com.adedom.teg.request.account.StateRequest
import com.adedom.teg.request.application.LogActiveRequest
import com.adedom.teg.request.application.RankPlayersRequest
import com.adedom.teg.request.single.ItemCollectionRequest
import com.adedom.teg.response.BackpackResponse
import com.adedom.teg.response.BaseResponse
import com.adedom.teg.response.PlayerResponse
import com.adedom.teg.response.RankPlayersResponse
import io.ktor.http.content.*

interface TegService {

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
