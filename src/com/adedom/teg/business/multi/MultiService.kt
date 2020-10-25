package com.adedom.teg.business.multi

import com.adedom.teg.models.request.MultiItemCollectionRequest
import com.adedom.teg.models.response.BaseResponse
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
interface MultiService {

    fun itemCollection(playerId: String?, multiItemCollectionRequest: MultiItemCollectionRequest): BaseResponse

}
