package com.adedom.teg.service.account

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
import com.adedom.teg.util.validateAccessToken
import io.ktor.http.content.*

class AccountServiceImpl(private val repository: TegRepository) : AccountService {

    override suspend fun changeImageProfile(playerId: String, multiPartData: MultiPartData): BaseResponse {
        return BaseResponse()
    }

    override fun fetchPlayerInfo(playerId: String?): PlayerResponse {
        val response = PlayerResponse()

        val message: String = when {
            // validate Null Or Blank
            playerId == null -> playerId.validateAccessToken()

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

    override fun playerState(playerId: String, stateRequest: StateRequest): BaseResponse {
        return BaseResponse()
    }

    override fun changePassword(playerId: String, changePasswordRequest: ChangePasswordRequest): BaseResponse {
        return BaseResponse()
    }

    override fun changeProfile(playerId: String, changeProfileRequest: ChangeProfileRequest): BaseResponse {
        return BaseResponse()
    }

    override fun fetchRankPlayers(rankPlayersRequest: RankPlayersRequest): RankPlayersResponse {
        return RankPlayersResponse()
    }

    override fun postLogActive(playerId: String, logActiveRequest: LogActiveRequest): BaseResponse {
        return BaseResponse()
    }

    override fun fetchItemCollection(playerId: String): BackpackResponse {
        return BackpackResponse()
    }

    override fun postItemCollection(playerId: String, itemCollectionRequest: ItemCollectionRequest): BaseResponse {
        return BaseResponse()
    }

}
