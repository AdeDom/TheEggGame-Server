package com.adedom.teg.business.multi

import com.adedom.teg.models.request.CreateRoomRequest
import com.adedom.teg.models.request.JoinRoomInfoRequest
import com.adedom.teg.models.request.MultiItemCollectionRequest
import com.adedom.teg.models.response.BaseResponse
import com.adedom.teg.models.response.RoomsResponse
import com.adedom.teg.models.websocket.RoomInfoOutgoing
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
interface MultiService {

    fun itemCollection(playerId: String?, multiItemCollectionRequest: MultiItemCollectionRequest): BaseResponse

    fun fetchRooms(): RoomsResponse

    fun createRoom(playerId: String?, createRoomRequest: CreateRoomRequest): BaseResponse

    fun fetchRoomInfo(accessToken: String?): RoomInfoOutgoing

    fun joinRoomInfo(playerId: String?, joinRoomInfoRequest: JoinRoomInfoRequest): BaseResponse

}
