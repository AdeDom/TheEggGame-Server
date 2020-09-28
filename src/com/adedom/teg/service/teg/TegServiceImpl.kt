package com.adedom.teg.service.teg

import com.adedom.teg.repositories.TegRepository
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

class TegServiceImpl(private val repository: TegRepository) : TegService {

    override suspend fun changeImageProfile(playerId: Int, multiPartData: MultiPartData): BaseResponse {
        return repository.changeImageProfile(playerId, multiPartData)
    }

    override fun fetchPlayerInfo(playerId: Int): PlayerResponse {
        return repository.fetchPlayerInfo(playerId)
    }

    override fun playerState(playerId: Int, stateRequest: StateRequest): BaseResponse {
        return repository.playerState(playerId, stateRequest)
    }

    override fun changePassword(playerId: Int, changePasswordRequest: ChangePasswordRequest): BaseResponse {
        return repository.changePassword(playerId, changePasswordRequest)
    }

    override fun changeProfile(playerId: Int, changeProfileRequest: ChangeProfileRequest): BaseResponse {
        return repository.changeProfile(playerId, changeProfileRequest)
    }

    override fun fetchRankPlayers(rankPlayersRequest: RankPlayersRequest): RankPlayersResponse {
        return repository.fetchRankPlayers(rankPlayersRequest)
    }

    override fun postLogActive(playerId: Int, logActiveRequest: LogActiveRequest): BaseResponse {
        return repository.postLogActive(playerId, logActiveRequest)
    }

    override fun fetchItemCollection(playerId: Int): BackpackResponse {
        return repository.fetchItemCollection(playerId)
    }

    override fun postItemCollection(playerId: Int, itemCollectionRequest: ItemCollectionRequest): BaseResponse {
        return repository.postItemCollection(playerId, itemCollectionRequest)
    }

}
