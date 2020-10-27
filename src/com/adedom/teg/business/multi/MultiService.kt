package com.adedom.teg.business.multi

import com.adedom.teg.models.request.MultiItemCollectionRequest
import com.adedom.teg.models.response.BaseResponse
import com.adedom.teg.models.response.RoomsResponse
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
interface MultiService {

    fun itemCollection(playerId: String?, multiItemCollectionRequest: MultiItemCollectionRequest): BaseResponse

    fun fetchRooms(): RoomsResponse

}
