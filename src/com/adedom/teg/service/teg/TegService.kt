package com.adedom.teg.service.teg

import com.adedom.teg.request.application.LogActiveRequest
import com.adedom.teg.request.application.RankPlayersRequest
import com.adedom.teg.request.single.ItemCollectionRequest
import com.adedom.teg.response.BackpackResponse
import com.adedom.teg.response.BaseResponse
import com.adedom.teg.response.RankPlayersResponse

interface TegService {

    fun fetchRankPlayers(rankPlayersRequest: RankPlayersRequest): RankPlayersResponse

    fun postLogActive(playerId: String, logActiveRequest: LogActiveRequest): BaseResponse

    fun fetchItemCollection(playerId: String): BackpackResponse

    fun postItemCollection(playerId: String, itemCollectionRequest: ItemCollectionRequest): BaseResponse

}
