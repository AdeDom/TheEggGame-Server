package com.adedom.teg.transaction

import com.adedom.teg.db.*
import com.adedom.teg.models.Multi
import com.adedom.teg.models.Player
import com.adedom.teg.models.Room
import com.adedom.teg.models.RoomInfo
import com.adedom.teg.request.*
import com.adedom.teg.response.BackpackResponse
import com.adedom.teg.response.MultiScoreResponse
import com.adedom.teg.util.CommonConstant
import com.adedom.teg.util.encryptSHA
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

object DatabaseTransaction {

    fun getCountPlayer(playerId: Int) = transaction {
        Players.select { Players.playerId eq playerId }
            .count()
            .toInt()
    }

    fun getCountRoom(roomNo: String) = transaction {
        Rooms.select { Rooms.roomNo eq roomNo }
            .count()
            .toInt()
    }

    fun getCountRoomInfo(roomNo: String) = transaction {
        RoomInfos.select { RoomInfos.roomNo eq roomNo }
            .count()
            .toInt()
    }

    fun getCountLogActive(logKey: String) = transaction {
        LogActives.select { LogActives.logKey eq logKey }
            .count()
            .toInt()
    }

    fun validatePasswordPlayer(putPassword: PutPassword): Boolean {
        val (playerId, oldPassword, _) = putPassword
        return transaction {
            val count = Players.select {
                Players.playerId eq playerId!! and (Players.password eq oldPassword.encryptSHA())
            }.count().toInt()

            count == 0
        }
    }

    fun getCountUsername(username: String) = transaction {
        Players.select { Players.username eq username }
            .count()
            .toInt()
    }

    fun getCountName(name: String) = transaction {
        Players.select { Players.name eq name }
            .count()
            .toInt()
    }

    fun getCountMulti(multiId: Int) = transaction {
        Multis.select { Multis.multiId eq multiId }
            .count()
            .toInt()
    }

    fun getCountPeopleRoom(roomNo: String): Boolean = transaction {
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

    fun getCountSignIn(postSignIn: PostSignIn): Boolean {
        val (username, password) = postSignIn
        return transaction {
            val count: Int = Players.select {
                Players.username eq username!! and (Players.password eq password.encryptSHA())
            }.count().toInt()

            count == 0
        }
    }

    fun getCountMultiRoomNo(roomNo: String): Boolean = transaction {
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

    //    todo create and return json to object
    fun getBackpack(playerId: Int): BackpackResponse = transaction {
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

        BackpackResponse(egg, eggI, eggII, eggIII)
    }

    fun getMultiScore(roomNo: String): MultiScoreResponse = transaction {
        val teamA = MultiCollections.select { MultiCollections.roomNo eq roomNo and (MultiCollections.team eq "A") }
            .count()
            .toInt()

        val teamB = MultiCollections.select { MultiCollections.roomNo eq roomNo and (MultiCollections.team eq "B") }
            .count()
            .toInt()

        MultiScoreResponse(teamA, teamB)
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

    fun postSignIn(postSignIn: PostSignIn): Int? {
        val (username, password) = postSignIn
        return transaction {
            Players.slice(Players.playerId)
                .select { Players.username eq username!! and (Players.password eq password.encryptSHA()) }
                .map { Players.toPlayerId(it) }
                .single()
                .playerId
        }
    }

    fun postSignUp(postSignUp: PostSignUp): Int? {
        val (username, password, name, gender) = postSignUp
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

    fun postLogActive(postLogActive: PostLogActive) {
        val (logKey, playerId) = postLogActive
        transaction {
            LogActives.insert {
                it[LogActives.logKey] = logKey!!
                it[LogActives.playerId] = playerId!!
                it[dateTimeIn] = DateTime.now()
                it[dateTimeOut] = DateTime.now()
            }
        }
    }

    fun postItemCollection(postItemCollection: PostItemCollection) {
        val (playerId, itemId, qty, latitude, longitude) = postItemCollection
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

    fun postMulti(postMulti: PostMulti) {
        val (roomNo, latitude, longitude) = postMulti
        transaction {
            Multis.insert {
                it[Multis.roomNo] = roomNo!!
                it[Multis.latitude] = latitude!!
                it[Multis.longitude] = longitude!!
                it[status] = "on"
            }
        }
    }

    fun postMultiCollection(postMultiCollection: PostMultiCollection) {
        val (multiId, roomNo, playerId, team, latitude, longitude) = postMultiCollection
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

    fun postRoom(postRoom: PostRoom): String {
        val (name, people, playerId) = postRoom
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

    fun postRoomInfo(postRoomInfo: PostRoomInfo) {
        val (roomNo, playerId) = postRoomInfo
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

    fun putLatLng(putLatlng: PutLatlng) {
        val (roomNo, playerId, latitude, longitude) = putLatlng
        transaction {
            RoomInfos.update({
                RoomInfos.roomNo eq roomNo!! and (RoomInfos.playerId eq playerId!!)
            }) {
                it[RoomInfos.latitude] = latitude!!
                it[RoomInfos.longitude] = longitude!!
            }
        }
    }

    fun putReady(putReady: PutReady) {
        val (roomNo, playerId, status) = putReady
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

    fun putState(putState: PutState) {
        val (playerId, state) = putState
        transaction {
            Players.update({ Players.playerId eq playerId!! }) {
                it[Players.state] = state!!
            }
        }
    }

    fun putTeam(putTeam: PutTeam) {
        val (roomNo, playerId, team) = putTeam
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

    fun putPassword(putPassword: PutPassword) {
        val (playerId, _, newPassword) = putPassword
        transaction {
            Players.update({
                Players.playerId eq playerId!!
            }) {
                it[password] = newPassword.encryptSHA()
            }
        }
    }

    fun putProfile(putProfile: PutProfile) {
        val (playerId, name, gender) = putProfile
        transaction {
            Players.update({
                Players.playerId eq playerId!!
            }) {
                it[Players.name] = name!!
                it[Players.gender] = gender!!
            }
        }
    }

    fun deletePlayerRoomInfo(deletePlayerRoomInfo: DeletePlayerRoomInfo) {
        val (roomNo, playerId) = deletePlayerRoomInfo
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
