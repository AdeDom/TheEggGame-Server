package com.adedom.teg.business.multi

import com.adedom.teg.models.request.ChangeTeamRequest
import com.adedom.teg.models.request.CreateRoomRequest
import com.adedom.teg.models.request.JoinRoomInfoRequest
import com.adedom.teg.models.request.MultiItemCollectionRequest
import com.adedom.teg.models.response.BaseResponse
import com.adedom.teg.models.response.CurrentRoomNoResponse
import com.adedom.teg.models.response.FetchMultiPlayerResponse
import com.adedom.teg.models.response.RoomsResponse
import com.adedom.teg.models.websocket.RoomInfoPlayersOutgoing
import com.adedom.teg.models.websocket.RoomInfoTitleOutgoing
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
interface MultiService {

    fun itemCollection(playerId: String?, multiItemCollectionRequest: MultiItemCollectionRequest): BaseResponse

    fun fetchRooms(): RoomsResponse

    fun createRoom(playerId: String?, createRoomRequest: CreateRoomRequest): BaseResponse

    fun fetchRoomInfoTitle(roomNo: String): RoomInfoTitleOutgoing

    fun fetchRoomInfoPlayers(roomNo: String): RoomInfoPlayersOutgoing

    fun currentRoomNo(playerId: String?): CurrentRoomNoResponse

    fun currentRoomNo(accessToken: String): String

    fun joinRoomInfo(playerId: String?, joinRoomInfoRequest: JoinRoomInfoRequest): BaseResponse

    fun leaveRoomInfo(playerId: String?): BaseResponse

    fun changeTeam(playerId: String?, changeTeamRequest: ChangeTeamRequest): BaseResponse

    fun changeStatusRoomInfo(playerId: String?): BaseResponse

    fun roomInfoTegMulti(playerId: String?): BaseResponse

    fun changeStatusUnready(playerId: String?): BaseResponse

    fun fetchMultiPlayer(playerId: String?): FetchMultiPlayerResponse

}
