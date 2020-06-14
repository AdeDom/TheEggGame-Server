package com.adedom.teg.transaction

import com.adedom.teg.db.*
import com.adedom.teg.models.*
import com.adedom.teg.request.*
import com.adedom.teg.util.CommonConstant
import com.adedom.teg.util.encryptSHA
import com.adedom.teg.util.jwt.PlayerPrincipal
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

object DatabaseTransaction {

    fun validatePlayer(playerId: Int): Boolean = transaction {
        val count = Players.select { Players.playerId eq playerId }
            .count()
            .toInt()

        count == 0
    }

    fun validateRoom(roomNo: String): Boolean = transaction {
        val count = Rooms.select { Rooms.roomNo eq roomNo }
            .count()
            .toInt()

        count == 0
    }

    fun validateRoomInfo(roomNo: String): Boolean = transaction {
        val count = RoomInfos.select { RoomInfos.roomNo eq roomNo }
            .count()
            .toInt()

        count == 0
    }

    fun validateLogActive(logKey: String): Boolean = transaction {
        val count = LogActives.select { LogActives.logKey eq logKey }
            .count()
            .toInt()

        count == 0
    }

    fun validatePasswordPlayer(passwordRequest: PasswordRequest): Boolean {
        val (playerId, oldPassword, _) = passwordRequest
        return transaction {
            val count = Players.select {
                Players.playerId eq playerId!! and (Players.password eq oldPassword.encryptSHA())
            }.count().toInt()

            count == 0
        }
    }

    fun validateUsername(username: String): Boolean = transaction {
        val count = Players.select { Players.username eq username }
            .count()
            .toInt()

        count == 0
    }

    fun validateName(name: String): Boolean = transaction {
        val count = Players.select { Players.name eq name }
            .count()
            .toInt()

        count == 0
    }

    fun validateMulti(multiId: Int): Boolean = transaction {
        val count = Multis.select { Multis.multiId eq multiId }
            .count()
            .toInt()

        count == 0
    }

    fun validatePeopleRoom(roomNo: String): Boolean = transaction {
        addLogger(StdOutSqlLogger)

        val peopleRoom: Int = Rooms.slice(Rooms.people)
            .select { Rooms.roomNo eq roomNo }
            .map { Rooms.toPeopleRoom(it) }
            .single()
            .people
            ?.toInt() ?: 0

        val peopleRoomInfo: Int = RoomInfos.select { RoomInfos.roomNo eq roomNo }
            .count()
            .toInt()

        peopleRoom < peopleRoomInfo
    }

    fun validateSignIn(signInRequest: SignInRequest): Boolean {
        val (username, password) = signInRequest
        return transaction {
            val count: Int = Players.select {
                Players.username eq username!! and (Players.password eq password.encryptSHA())
            }.count().toInt()

            count == 0
        }
    }

    fun validateMultiRoomNo(roomNo: String): Boolean = transaction {
        val count = Multis.select { Multis.roomNo eq roomNo }
            .count()
            .toInt()

        count == 0
    }

    fun getPlayer(playerId: Int): Player = transaction {
        (Players innerJoin ItemCollections).slice(
            Players.playerId,
            Players.username,
            Players.name,
            Players.image,
            ItemCollections.level,
            Players.state,
            Players.gender
        ).select { Players.playerId eq playerId }
            .map { MapResponse.toPlayer(it) }
            .single()
    }

    fun getMultis(roomNo: String): List<Multi> = transaction {
        Multis.select { Multis.roomNo eq roomNo }
            .map { Multis.toMulti(it) }
    }

    fun getBackpack(playerId: Int): Backpack = transaction {
        val egg = ItemCollections.select { ItemCollections.playerId eq playerId and (ItemCollections.itemId eq 1) }
            .map { ItemCollections.toItemCollection(it) }
            .sumBy { it.qty!! } / 1000

        val eggI = ItemCollections.select { ItemCollections.playerId eq playerId and (ItemCollections.itemId eq 2) }
            .map { ItemCollections.toItemCollection(it) }
            .sumBy { it.qty!! }

        val eggII = ItemCollections.select { ItemCollections.playerId eq playerId and (ItemCollections.itemId eq 3) }
            .map { ItemCollections.toItemCollection(it) }
            .sumBy { it.qty!! }

        val eggIII = ItemCollections.select { ItemCollections.playerId eq playerId and (ItemCollections.itemId eq 4) }
            .map { ItemCollections.toItemCollection(it) }
            .sumBy { it.qty!! }

        Backpack(egg, eggI, eggII, eggIII)
    }

    fun getMultiScore(roomNo: String): Score = transaction {
        val teamA = MultiCollections.select { MultiCollections.roomNo eq roomNo and (MultiCollections.team eq "A") }
            .count()
            .toInt()

        val teamB = MultiCollections.select { MultiCollections.roomNo eq roomNo and (MultiCollections.team eq "B") }
            .count()
            .toInt()

        Score(teamA, teamB)
    }

    fun getPlayers(search: String, limit: Int): List<Player> = transaction {
        addLogger(StdOutSqlLogger)

        val query = (Players innerJoin ItemCollections)
            .slice(
                Players.playerId,
                Players.username,
                Players.name,
                Players.image,
                ItemCollections.level,
                Players.state,
                Players.gender
            )
            .select { ItemCollections.itemId eq 1 and (Players.name like "%${search}%") }
            .groupBy(Players.playerId)
            .orderBy(ItemCollections.level to SortOrder.DESC, (Players.playerId to SortOrder.ASC))

        when (limit) {
            CommonConstant.LIMIT_TEN -> query.limit(CommonConstant.LIMIT_TEN)
            CommonConstant.LIMIT_FIFTY -> query.limit(CommonConstant.LIMIT_FIFTY)
            CommonConstant.LIMIT_ONE_HUNDRED -> query.limit(CommonConstant.LIMIT_ONE_HUNDRED)
        }

        query.map { Players.toPlayers(it) }

    }

    fun getRooms(): List<Room> = transaction {
        Rooms.select { Rooms.status eq "on" }
            .orderBy(Rooms.roomId to SortOrder.ASC)
            .map { Rooms.toRoom(it) }
    }

    fun getRoomInfos(roomNo: String): List<RoomInfo> = transaction {
        (Players innerJoin ItemCollections innerJoin RoomInfos)
            .slice(
                RoomInfos.roomNo,
                RoomInfos.latitude,
                RoomInfos.longitude,
                RoomInfos.team,
                RoomInfos.status,
                Players.playerId,
                Players.name,
                Players.image,
                ItemCollections.qty.sum(),
                Players.state,
                Players.gender
            ).select { RoomInfos.roomNo eq roomNo }
            .groupBy(Players.playerId)
            .orderBy(RoomInfos.infoId to SortOrder.ASC)
            .map { MapResponse.toRoomInfo(it) }
    }

    fun postSignIn(signInRequest: SignInRequest): PlayerPrincipal {
        val (username, password) = signInRequest
        return transaction {
            Players.slice(Players.playerId, Players.username)
                .select { Players.username eq username!! and (Players.password eq password.encryptSHA()) }
                .map { MapResponse.toPlayerPrincipal(it) }
                .single()
        }
    }

    fun postSignUp(signUpRequest: SignUpRequest): Int? {
        val (username, password, name, gender) = signUpRequest
        return transaction {
            Players.insert {
                it[Players.username] = username!!
                it[Players.password] = password.encryptSHA()
                it[Players.name] = name!!
                it[image] = "empty"
                it[state] = "online"
                it[Players.gender] = gender!!
                it[dateTime] = DateTime.now()
            }

            Players.slice(Players.playerId)
                .select { Players.username eq username!! and (Players.password eq password.encryptSHA()) }
                .map { Players.toPlayerId(it) }
                .single()
                .playerId
        }
    }

    fun postLogActive(logActiveRequest: LogActiveRequest) {
        val (logKey, playerId) = logActiveRequest
        transaction {
            LogActives.insert {
                it[LogActives.logKey] = logKey!!
                it[LogActives.playerId] = playerId!!
                it[dateTimeIn] = DateTime.now()
                it[dateTimeOut] = DateTime.now()
            }
        }
    }

    fun postItemCollection(itemCollectionRequest: ItemCollectionRequest) {
        val (playerId, itemId, qty, latitude, longitude) = itemCollectionRequest
        transaction {
            ItemCollections.insert {
                it[ItemCollections.playerId] = playerId!!
                it[ItemCollections.itemId] = itemId!!
                it[ItemCollections.qty] = qty!!
                it[ItemCollections.latitude] = latitude!!
                it[ItemCollections.longitude] = longitude!!
                it[dateTime] = DateTime.now()
            }
        }
    }

    fun postMulti(multiRequest: MultiRequest) {
        val (roomNo, latitude, longitude) = multiRequest
        transaction {
            Multis.insert {
                it[Multis.roomNo] = roomNo!!
                it[Multis.latitude] = latitude!!
                it[Multis.longitude] = longitude!!
                it[status] = "on"
            }
        }
    }

    fun postMultiCollection(multiCollectionRequest: MultiCollectionRequest) {
        val (multiId, roomNo, playerId, team, latitude, longitude) = multiCollectionRequest
        transaction {
            Multis.update({ Multis.multiId eq multiId!! }) {
                it[status] = "off"
            }
            MultiCollections.insert {
                it[MultiCollections.roomNo] = roomNo!!
                it[MultiCollections.playerId] = playerId!!
                it[score] = 0
                it[MultiCollections.team] = team!!
                it[MultiCollections.latitude] = latitude!!
                it[MultiCollections.longitude] = longitude!!
                it[dateTime] = DateTime.now()
            }
        }
    }

    fun postRoom(roomRequest: RoomRequest): String {
        val (name, people, playerId) = roomRequest
        return transaction {
            addLogger(StdOutSqlLogger)

            val roomNo: String = (Rooms.slice(Rooms.roomNo)
                .selectAll()
                .orderBy(Rooms.roomId to SortOrder.DESC)
                .limit(1)
                .map { Rooms.toRoomNo(it) }
                .single()
                .roomNo
                ?.toInt()
                ?.plus(1) ?: 1)
                .toString()

            Rooms.insert {
                it[Rooms.roomNo] = roomNo
                it[Rooms.name] = name!!
                it[Rooms.people] = people!!
                it[status] = "on"
                it[dateTime] = DateTime.now()
            }

            RoomInfos.insert {
                it[RoomInfos.roomNo] = roomNo
                it[RoomInfos.playerId] = playerId!!
                it[latitude] = 0.0
                it[longitude] = 0.0
                it[team] = "A"
                it[status] = "unready"
                it[dateTime] = DateTime.now()
            }

            roomNo
        }
    }

    fun postRoomInfo(roomInfoRequest: RoomInfoRequest) {
        val (roomNo, playerId) = roomInfoRequest
        transaction {
            RoomInfos.insert {
                it[RoomInfos.roomNo] = roomNo!!
                it[RoomInfos.playerId] = playerId!!
                it[latitude] = 0.0
                it[longitude] = 0.0
                it[team] = "B"
                it[status] = "unready"
                it[dateTime] = DateTime.now()
            }
        }
    }

    fun putLatLng(latlngRequest: LatlngRequest) {
        val (roomNo, playerId, latitude, longitude) = latlngRequest
        transaction {
            RoomInfos.update({
                RoomInfos.roomNo eq roomNo!! and (RoomInfos.playerId eq playerId!!)
            }) {
                it[RoomInfos.latitude] = latitude!!
                it[RoomInfos.longitude] = longitude!!
            }
        }
    }

    fun putReady(readyRequest: ReadyRequest) {
        val (roomNo, playerId, status) = readyRequest
        transaction {
            RoomInfos.update({
                RoomInfos.roomNo eq roomNo!! and (RoomInfos.playerId eq playerId!!)
            }) {
                it[RoomInfos.status] = status!!
            }
        }
    }

    fun putRoomOff(roomNo: String) = transaction {
        Rooms.update({ Rooms.roomNo eq roomNo }) {
            it[status] = "off"
        }
    }

    fun putState(stateRequest: StateRequest) {
        val (playerId, state) = stateRequest
        transaction {
            Players.update({ Players.playerId eq playerId!! }) {
                it[Players.state] = state!!
            }
        }
    }

    fun putTeam(teamRequest: TeamRequest) {
        val (roomNo, playerId, team) = teamRequest
        transaction {
            RoomInfos.update({
                RoomInfos.roomNo eq roomNo!! and (RoomInfos.playerId eq playerId!!)
            }) {
                it[RoomInfos.team] = team!!
            }
        }
    }

    fun putLogActive(logKey: String) {
        transaction {
            LogActives.update({
                LogActives.logKey eq logKey
            }) {
                it[dateTimeOut] = DateTime.now()
            }
        }
    }

    fun putPassword(passwordRequest: PasswordRequest) {
        val (playerId, _, newPassword) = passwordRequest
        transaction {
            Players.update({
                Players.playerId eq playerId!!
            }) {
                it[password] = newPassword.encryptSHA()
            }
        }
    }

    fun putProfile(profileRequest: ProfileRequest) {
        val (playerId, name, gender) = profileRequest
        transaction {
            Players.update({
                Players.playerId eq playerId!!
            }) {
                it[Players.name] = name!!
                it[Players.gender] = gender!!
            }
        }
    }

    fun deletePlayerRoomInfo(roomInfoRequest: RoomInfoRequest) {
        val (roomNo, playerId) = roomInfoRequest
        transaction {
            addLogger(StdOutSqlLogger)

            val count = RoomInfos.select { RoomInfos.roomNo eq roomNo!! }
                .count()
                .toInt()

            if (count == 1) Rooms.deleteWhere { Rooms.roomNo eq roomNo!! }

            RoomInfos.deleteWhere {
                RoomInfos.roomNo eq roomNo!! and (RoomInfos.playerId eq playerId!!)
            }
        }
    }

}
