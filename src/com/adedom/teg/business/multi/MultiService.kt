package com.adedom.teg.business.multi

import com.adedom.teg.models.request.CreateRoomRequest
import com.adedom.teg.models.request.JoinRoomInfoRequest
import com.adedom.teg.models.request.MultiItemCollectionRequest
import com.adedom.teg.models.response.BaseResponse
import com.adedom.teg.models.response.RoomsResponse
import com.adedom.teg.models.websocket.RoomInfoPlayersOutgoing
import com.adedom.teg.models.websocket.RoomInfoTitleOutgoing
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
interface MultiService {

    fun itemCollection(playerId: String?, multiItemCollectionRequest: MultiItemCollectionRequest): BaseResponse

    fun fetchRooms(): RoomsResponse

    fun createRoom(playerId: String?, createRoomRequest: CreateRoomRequest): BaseResponse

    fun fetchRoomInfoTitle(accessToken: String?): RoomInfoTitleOutgoing

    fun fetchRoomInfoPlayers(accessToken: String?): RoomInfoPlayersOutgoing

    fun joinRoomInfo(playerId: String?, joinRoomInfoRequest: JoinRoomInfoRequest): BaseResponse

}
