package com.adedom.teg.service.account

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

interface AccountService {

    suspend fun changeImageProfile(playerId: String, multiPartData: MultiPartData): BaseResponse

    fun fetchPlayerInfo(playerId: String?): PlayerResponse

    fun playerState(playerId: String, stateRequest: StateRequest): BaseResponse

    fun changePassword(playerId: String, changePasswordRequest: ChangePasswordRequest): BaseResponse

    fun changeProfile(playerId: String, changeProfileRequest: ChangeProfileRequest): BaseResponse

    fun fetchRankPlayers(rankPlayersRequest: RankPlayersRequest): RankPlayersResponse

    fun postLogActive(playerId: String, logActiveRequest: LogActiveRequest): BaseResponse

    fun fetchItemCollection(playerId: String): BackpackResponse

    fun postItemCollection(playerId: String, itemCollectionRequest: ItemCollectionRequest): BaseResponse

}
