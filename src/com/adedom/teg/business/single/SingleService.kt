package com.adedom.teg.business.single

import com.adedom.teg.models.request.SingleItemRequest
import com.adedom.teg.models.response.BackpackResponse
import com.adedom.teg.models.response.BaseResponse
import com.adedom.teg.models.websocket.SingleItemOutgoing
import com.adedom.teg.models.websocket.SingleSuccessAnnouncementOutgoing
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
interface SingleService {

    fun fetchItemCollection(playerId: String?): BackpackResponse

    fun itemCollection(playerId: String?, singleItemRequest: SingleItemRequest): BaseResponse

    fun singleItem(accessToken: String): SingleItemOutgoing

    fun singleSuccessAnnouncement(accessToken: String): SingleSuccessAnnouncementOutgoing

}
