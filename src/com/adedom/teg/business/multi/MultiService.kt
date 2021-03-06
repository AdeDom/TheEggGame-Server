package com.adedom.teg.business.multi

import com.adedom.teg.models.request.*
import com.adedom.teg.models.response.*
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

    fun currentRoomNo(accessToken: String): String

    fun joinRoomInfo(playerId: String?, joinRoomInfoRequest: JoinRoomInfoRequest): BaseResponse

    fun leaveRoomInfo(playerId: String?): BaseResponse

    fun changeTeam(playerId: String?, changeTeamRequest: ChangeTeamRequest): BaseResponse

    fun changeStatusRoomInfo(playerId: String?): BaseResponse

    fun roomInfoTegMulti(playerId: String?): BaseResponse

    fun changeStatusUnready(playerId: String?): BaseResponse

    fun fetchMultiPlayer(playerId: String?): FetchMultiPlayerResponse

    fun fetchMultiScore(playerId: String?): ScoreResponse

    fun addMultiScore(playerId: String?, addMultiScoreRequest: AddMultiScoreRequest): BaseResponse

    fun fetchMultiItem(playerId: String?): MultiItemResponse

    fun addMultiItem(playerId: String?): BaseResponse

    fun fetchMultiPlayerEndGame(playerId: String?): MultiPlayerEndGameResponse

    fun multiPlayerEndGame(playerId: String?): BaseResponse

}
