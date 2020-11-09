package com.adedom.teg.data.repositories

import com.adedom.teg.data.database.*
import com.adedom.teg.data.map.MapObject
import com.adedom.teg.data.models.*
import com.adedom.teg.models.request.*
import com.adedom.teg.util.TegConstant
import io.ktor.locations.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

@KtorExperimentalLocationsAPI
class TegRepositoryImpl : TegRepository {

    override fun isUsernameRepeat(username: String): Boolean {
        return transaction {
            Players.select { Players.username eq username }
                .count().toInt() > 0
        }
    }

    override fun isNameRepeat(name: String): Boolean {
        return transaction {
            Players.select { Players.name eq name }
                .count().toInt() > 0
        }
    }

    override fun isValidateSignIn(signInRequest: SignInRequest): Boolean {
        val (username, password) = signInRequest
        return transaction {
            Players.select {
                Players.username eq username!! and (Players.password eq password!!)
            }.count().toInt() == 0
        }
    }

    override fun isValidateChangePassword(playerId: String, changePasswordRequest: ChangePasswordRequest): Boolean {
        val (oldPassword) = changePasswordRequest
        return transaction {
            Players.select {
                Players.playerId eq playerId and (Players.password eq oldPassword!!)
            }.count().toInt() == 0
        }
    }

    override fun isValidateRoomNoOnReady(roomNo: String): Boolean {
        return transaction {
            Rooms.slice(Rooms.roomNo)
                .select {
                    Rooms.status eq TegConstant.ROOM_STATUS_ON and (Rooms.roomNo eq roomNo)
                }.count().toInt() == 0
        }
    }

    override fun isValidatePeopleRoomInfo(roomNo: String): Boolean {
        return transaction {
            addLogger(StdOutSqlLogger)

            val peopleRoom: Int = Rooms.slice(Rooms.people)
                .select { Rooms.roomNo eq roomNo }
                .map { it[Rooms.people] }
                .single()
                .toInt()

            val peopleRoomInfo: Int = RoomInfos.select { RoomInfos.roomNo eq roomNo }
                .count()
                .toInt()

            peopleRoom <= peopleRoomInfo
        }
    }

    override fun getMissionDateTimeLast(playerId: String, modeMission: String): Long {
        return transaction {
            try {
                ItemCollections
                    .slice(ItemCollections.dateTime)
                    .select {
                        ItemCollections.playerId eq playerId and (ItemCollections.mode eq modeMission)
                    }.orderBy(ItemCollections.dateTime, SortOrder.DESC)
                    .limit(1)
                    .single()
                    .get(ItemCollections.dateTime)
            } catch (e: NoSuchElementException) {
                0L
            }
        }
    }

    override fun signIn(signInRequest: SignInRequest): PlayerIdDb {
        val (username, password) = signInRequest
        return transaction {
            Players.slice(Players.playerId)
                .select { Players.username eq username!! and (Players.password eq password!!) }
                .map { MapObject.toPlayerIdDb(it) }
                .single()
        }
    }

    override fun signUp(signUp: SignUpDb): Pair<Boolean, String> {
        val (username, password, name, gender, birthDate) = signUp

        val statement = transaction {
            Players.insert {
                it[Players.playerId] = UUID.randomUUID().toString().replace("-", "")
                it[Players.username] = username!!
                it[Players.password] = password!!
                it[Players.name] = name!!
                it[Players.gender] = gender!!
                it[Players.birthDate] = birthDate!!
                it[Players.dateTimeCreated] = System.currentTimeMillis()
            }
        }

        val resulted = statement.resultedValues?.size == 1
        val playerId = statement.resultedValues?.get(0)?.get(Players.playerId)

        return if (playerId.isNullOrBlank()) Pair(false, "") else Pair(resulted, playerId)
    }

    //    todo resize image
    override fun changeImageProfile(playerId: String, imageName: String): Boolean {
        val transaction: Int = transaction {
            Players.update({ Players.playerId eq playerId }) {
                it[image] = TegConstant.BASE_IMAGE + imageName
                it[dateTimeUpdated] = System.currentTimeMillis()
            }
        }
        return transaction == 1
    }

    override fun fetchPlayerInfo(playerId: String): PlayerInfoDb {
        return transaction {
            (Players innerJoin ItemCollections).slice(
                Players.playerId,
                Players.username,
                Players.name,
                Players.image,
                ItemCollections.qty.sum(),
                Players.state,
                Players.gender,
                Players.birthDate,
            ).select { Players.playerId eq playerId }
                .map { MapObject.toPlayerInfoDb(it) }
                .single()
        }
    }

    override fun playerState(playerId: String, stateRequest: StateRequest): Boolean {
        val (state) = stateRequest
        val transaction: Int = transaction {
            Players.update({ Players.playerId eq playerId }) {
                it[Players.state] = state
            }
        }
        return transaction == 1
    }

    override fun changePassword(playerId: String, changePasswordRequest: ChangePasswordRequest): Boolean {
        val (_, newPassword) = changePasswordRequest
        val transaction: Int = transaction {
            Players.update({ Players.playerId eq playerId }) {
                it[password] = newPassword!!
                it[dateTimeUpdated] = System.currentTimeMillis()
            }
        }
        return transaction == 1
    }

    override fun changeProfile(playerId: String, changeProfile: ChangeProfileDb): Boolean {
        val (name, gender, birthDate) = changeProfile
        val transaction: Int = transaction {
            Players.update({ Players.playerId eq playerId }) {
                it[Players.name] = name!!
                it[Players.gender] = gender!!
                it[Players.birthDate] = birthDate!!
                it[Players.dateTimeUpdated] = System.currentTimeMillis()
            }
        }
        return transaction == 1
    }

    override fun changeLatLng(playerId: String, changeLatLngRequest: ChangeLatLngRequest): Boolean {
        val (latitude, longitude) = changeLatLngRequest

        val result = transaction {
            Players.update({
                Players.playerId eq playerId
            }) {
                it[Players.latitude] = latitude
                it[Players.longitude] = longitude
            }
        }

        return result == 1
    }

    override fun fetchRankPlayers(rankPlayersRequest: RankPlayersRequest): List<PlayerInfoDb> {
        val (_, search, limit) = rankPlayersRequest

        return transaction {
            addLogger(StdOutSqlLogger)

            val query = (Players innerJoin ItemCollections)
                .slice(
                    Players.playerId,
                    Players.username,
                    Players.name,
                    Players.image,
                    ItemCollections.qty.sum(),
                    Players.state,
                    Players.gender,
                    Players.birthDate,
                )
                .select { ItemCollections.itemId eq 1 and (Players.name like "%${search}%") }
                .groupBy(Players.playerId)
                .orderBy(ItemCollections.qty.sum() to SortOrder.DESC, Players.playerId to SortOrder.ASC)

            when (limit!!.toInt()) {
                TegConstant.RANK_LIMIT_TEN -> query.limit(TegConstant.RANK_LIMIT_TEN)
                TegConstant.RANK_LIMIT_FIFTY -> query.limit(TegConstant.RANK_LIMIT_FIFTY)
                TegConstant.RANK_LIMIT_HUNDRED -> query.limit(TegConstant.RANK_LIMIT_HUNDRED)
            }

            query.map { MapObject.toPlayerInfoDb(it) }
        }
    }

    override fun logActiveOn(playerId: String): Boolean {
        val statement = transaction {
            LogActives.insert {
                it[LogActives.playerId] = playerId
                it[LogActives.dateTimeIn] = System.currentTimeMillis()
            }
        }

        return statement.resultedValues?.size == 1
    }

    override fun logActiveOff(playerId: String): Boolean {
        val transaction = transaction {
            val logActive = LogActives.slice(LogActives.logId)
                .select { LogActives.playerId eq playerId }
                .orderBy(LogActives.logId to SortOrder.DESC)
                .limit(1)
                .map { MapObject.toLogActiveLogIdDb(it) }
                .single()

            LogActives.update({ LogActives.logId eq logActive.logId }) {
                it[LogActives.dateTimeOut] = System.currentTimeMillis()
            }
        }

        return transaction == 1
    }

    override fun fetchItemCollection(playerId: String): BackpackDb {
        return transaction {
            addLogger(StdOutSqlLogger)

            val eggI =
                ItemCollections.select { ItemCollections.playerId eq playerId and (ItemCollections.itemId eq TegConstant.SINGLE_ITEM_ONE) }
                    .map { MapObject.toItemCollectionDb(it) }
                    .sumBy { it.qty!! }

            val eggII =
                ItemCollections.select { ItemCollections.playerId eq playerId and (ItemCollections.itemId eq TegConstant.SINGLE_ITEM_TWO) }
                    .map { MapObject.toItemCollectionDb(it) }
                    .sumBy { it.qty!! }

            val eggIII =
                ItemCollections.select { ItemCollections.playerId eq playerId and (ItemCollections.itemId eq TegConstant.SINGLE_ITEM_THREE) }
                    .map { MapObject.toItemCollectionDb(it) }
                    .sumBy { it.qty!! }

            BackpackDb(eggI, eggII, eggIII)
        }
    }

    override fun itemCollection(
        playerId: String,
        modeMission: String,
        itemCollectionRequest: ItemCollectionRequest
    ): Boolean {
        val (itemId, qty, latitude, longitude) = itemCollectionRequest

        val statement = transaction {
            ItemCollections.insert {
                it[ItemCollections.playerId] = playerId
                it[ItemCollections.itemId] = itemId!!
                it[ItemCollections.qty] = qty!!
                it[ItemCollections.latitude] = latitude!!
                it[ItemCollections.longitude] = longitude!!
                it[ItemCollections.dateTime] = System.currentTimeMillis()
                it[ItemCollections.mode] = modeMission
            }
        }

        return statement.resultedValues?.size == 1
    }

    override fun fetchMissionSingle(playerId: String): List<Long> {
        return transaction {
            addLogger(StdOutSqlLogger)

            ItemCollections
                .slice(ItemCollections.dateTime)
                .select {
                    ItemCollections.playerId eq playerId and (ItemCollections.mode eq TegConstant.ITEM_COLLECTION_SINGLE)
                }.orderBy(ItemCollections.dateTime, SortOrder.DESC)
                .limit(TegConstant.MISSION_SINGLE_QTY)
                .map { it[ItemCollections.dateTime] }
        }
    }

    override fun fetchMissionMulti(playerId: String): Long {
        return transaction {
            addLogger(StdOutSqlLogger)

            try {
                ItemCollections
                    .slice(ItemCollections.dateTime)
                    .select {
                        ItemCollections.playerId eq playerId and (ItemCollections.mode eq TegConstant.ITEM_COLLECTION_MULTI)
                    }.orderBy(ItemCollections.dateTime, SortOrder.DESC)
                    .limit(1)
                    .map { it[ItemCollections.dateTime] }
                    .single()
            } catch (e: NoSuchElementException) {
                0L
            }
        }
    }

    override fun missionMain(playerId: String, missionRequest: MissionRequest): Boolean {
        val (mode) = missionRequest

        val statement = transaction {
            ItemCollections.insert {
                it[ItemCollections.playerId] = playerId
                it[ItemCollections.itemId] = TegConstant.ITEM_LEVEL
                it[ItemCollections.qty] = TegConstant.MISSION_QTY
                it[ItemCollections.latitude] = 0.0
                it[ItemCollections.longitude] = 0.0
                it[ItemCollections.dateTime] = System.currentTimeMillis()
                it[ItemCollections.mode] = mode!!
            }
        }

        return statement.resultedValues?.size == 1
    }

    override fun fetchRooms(): List<RoomDb> {
        return transaction {
            Rooms.select {
                Rooms.status eq TegConstant.ROOM_STATUS_ON
            }.map { MapObject.toRoomDb(it) }
        }
    }

    override fun createRoom(playerId: String, createRoomRequest: CreateRoomRequest): Boolean {
        val (roomName, roomPeople) = createRoomRequest

        val statement = transaction {
            addLogger(StdOutSqlLogger)

            var roomNo = 1
            try {
                roomNo = Rooms.slice(Rooms.roomNo)
                    .selectAll()
                    .orderBy(Rooms.roomId to SortOrder.DESC)
                    .limit(1)
                    .map { it[Rooms.roomNo] }
                    .single()
                    .toInt()
                    .plus(1)
            } catch (e: NoSuchElementException) {
            }

            Rooms.insert {
                it[Rooms.roomNo] = roomNo.toString()
                it[Rooms.name] = roomName!!
                it[Rooms.people] = roomPeople.toString()
                it[Rooms.status] = TegConstant.ROOM_STATUS_ON
                it[Rooms.dateTime] = System.currentTimeMillis()
            }

            // lat lng player
            val (latitude, longitude) = Players.slice(Players.latitude, Players.longitude)
                .select {
                    Players.playerId eq playerId
                }
                .map { Pair(it[Players.latitude], it[Players.longitude]) }
                .single()

            RoomInfos.insert {
                it[RoomInfos.roomNo] = roomNo.toString()
                it[RoomInfos.playerId] = playerId
                it[RoomInfos.latitude] = latitude
                it[RoomInfos.longitude] = longitude
                it[RoomInfos.team] = TegConstant.TEAM_A
                it[RoomInfos.status] = TegConstant.ROOM_UNREADY
                it[RoomInfos.role] = TegConstant.ROOM_ROLE_HEAD
                it[RoomInfos.dateTime] = System.currentTimeMillis()
            }
        }

        return statement.resultedValues?.size ?: 0 > 0
    }

    override fun fetchRoomInfoTitle(playerId: String): RoomDb {
        return transaction {
            addLogger(StdOutSqlLogger)

            var roomNo = ""
            try {
                roomNo = RoomInfos.slice(RoomInfos.roomNo)
                    .select {
                        RoomInfos.playerId eq playerId
                    }
                    .orderBy(RoomInfos.dateTime, SortOrder.DESC)
                    .limit(1)
                    .map { it[RoomInfos.roomNo] }
                    .single()
            } catch (e: NoSuchElementException) {
            }

            Rooms
                .select {
                    Rooms.roomNo eq roomNo
                }
                .map { MapObject.toRoomDb(it) }
                .single()
        }
    }

    override fun fetchRoomInfoPlayers(playerId: String): List<RoomInfoPlayersDb> {
        return transaction {
            addLogger(StdOutSqlLogger)

            val roomNo = RoomInfos.slice(RoomInfos.roomNo)
                .select {
                    RoomInfos.playerId eq playerId
                }
                .orderBy(RoomInfos.dateTime, SortOrder.DESC)
                .limit(1)
                .map { it[RoomInfos.roomNo] }
                .single()

            val roomInfoDb = RoomInfos
                .select {
                    RoomInfos.roomNo eq roomNo
                }
                .orderBy(RoomInfos.dateTime, SortOrder.ASC)
                .map { MapObject.toRoomInfoDb(it) }

            val playerInfoList = mutableListOf<RoomInfoPlayersDb>()
            roomInfoDb.forEach { roomInfo ->
                val playersDb = (Players innerJoin ItemCollections).slice(
                    Players.playerId,
                    Players.username,
                    Players.name,
                    Players.image,
                    ItemCollections.qty.sum(),
                    Players.state,
                    Players.gender,
                    Players.birthDate,
                ).select { Players.playerId eq roomInfo.playerId!! }
                    .map { MapObject.toRoomInfoPlayersDb(it, roomInfo.role, roomInfo.status, roomInfo.team) }
                    .single()
                playerInfoList.add(playersDb)
            }
            playerInfoList
        }
    }

    override fun joinRoomInfo(playerId: String, joinRoomInfoRequest: JoinRoomInfoRequest): Boolean {
        val (roomNo) = joinRoomInfoRequest

        val statement = transaction {
            val (latitude, longitude) = Players.slice(Players.latitude, Players.longitude)
                .select {
                    Players.playerId eq playerId
                }
                .map { Pair(it[Players.latitude], it[Players.longitude]) }
                .single()

            RoomInfos.insert {
                it[RoomInfos.roomNo] = roomNo.toString()
                it[RoomInfos.playerId] = playerId
                it[RoomInfos.latitude] = latitude
                it[RoomInfos.longitude] = longitude
                it[RoomInfos.team] = TegConstant.TEAM_B
                it[RoomInfos.status] = TegConstant.ROOM_UNREADY
                it[RoomInfos.role] = TegConstant.ROOM_ROLE_TAIL
                it[RoomInfos.dateTime] = System.currentTimeMillis()
            }
        }

        return statement.resultedValues?.size == 1
    }

}
