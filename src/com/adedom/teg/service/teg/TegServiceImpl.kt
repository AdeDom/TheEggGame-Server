package com.adedom.teg.service.teg

import com.adedom.teg.repositories.TegRepository
import com.adedom.teg.request.application.LogActiveRequest
import com.adedom.teg.request.application.RankPlayersRequest
import com.adedom.teg.request.single.ItemCollectionRequest
import com.adedom.teg.response.BackpackResponse
import com.adedom.teg.response.BaseResponse
import com.adedom.teg.response.RankPlayersResponse

class TegServiceImpl(private val repository: TegRepository) : TegService {

    override fun fetchRankPlayers(rankPlayersRequest: RankPlayersRequest): RankPlayersResponse {
        return repository.fetchRankPlayers(rankPlayersRequest)
    }

    override fun postLogActive(playerId: String, logActiveRequest: LogActiveRequest): BaseResponse {
        return repository.postLogActive(playerId, logActiveRequest)
    }

    override fun fetchItemCollection(playerId: String): BackpackResponse {
        return repository.fetchItemCollection(playerId)
    }

    override fun postItemCollection(playerId: String, itemCollectionRequest: ItemCollectionRequest): BaseResponse {
        return repository.postItemCollection(playerId, itemCollectionRequest)
    }

}
