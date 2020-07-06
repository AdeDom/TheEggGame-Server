package com.adedom.teg.transaction

import com.adedom.teg.db.*
import com.adedom.teg.models.*
import com.adedom.teg.request.*
import com.adedom.teg.util.toLevel
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

    fun validateMultiRoomNo(roomNo: String): Boolean = transaction {
        val count = Multis.select { Multis.roomNo eq roomNo }
            .count()
            .toInt()

        count == 0
    }

    fun getMultis(roomNo: String): List<Multi> = transaction {
        Multis.select { Multis.roomNo eq roomNo }
            .map { Multis.toMulti(it) }
    }

    fun getBackpack(playerId: Int): Backpack = transaction {
        val egg = ItemCollections.select { ItemCollections.playerId eq playerId and (ItemCollections.itemId eq 1) }
            .map { ItemCollections.toItemCollection(it) }
            .sumBy { it.qty!! }.toLevel()

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
                ItemCollections.level,
                Players.state,
                Players.gender
            ).select { RoomInfos.roomNo eq roomNo }
            .groupBy(Players.playerId)
            .orderBy(RoomInfos.infoId to SortOrder.ASC)
            .map { MapResponse.toRoomInfo(it) }
    }

    fun postItemCollection(playerId: Int, itemCollectionRequest: ItemCollectionRequest) {
        val (itemId, qty, latitude, longitude) = itemCollectionRequest
        transaction {
            ItemCollections.insert {
                it[ItemCollections.playerId] = playerId
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

    fun patchLogActive(logKey: String) = transaction {
        LogActives.update({ LogActives.logKey eq logKey }) {
            it[dateTimeOut] = DateTime.now()
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
