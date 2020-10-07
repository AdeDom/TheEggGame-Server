package com.adedom.teg.refactor

import com.adedom.teg.data.database.*
import com.adedom.teg.data.map.MapObject
import com.adedom.teg.data.models.MultiDb
import com.adedom.teg.data.models.RoomDb
import com.adedom.teg.data.models.RoomInfoDb
import com.adedom.teg.data.models.ScoreDb
import com.adedom.teg.models.request.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseTransaction {

    fun validatePlayer(playerId: String): Boolean = transaction {
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
            .map { MapObject.toPeopleRoomDb(it) }
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

    fun getMultis(roomNo: String): List<MultiDb> = transaction {
        Multis.select { Multis.roomNo eq roomNo }
            .map { MapObject.toMultiDb(it) }
    }

    fun getMultiScore(roomNo: String): ScoreDb = transaction {
        val teamA = MultiCollections.select { MultiCollections.roomNo eq roomNo and (MultiCollections.team eq "A") }
            .count()
            .toInt()

        val teamB = MultiCollections.select { MultiCollections.roomNo eq roomNo and (MultiCollections.team eq "B") }
            .count()
            .toInt()

        ScoreDb(teamA, teamB)
    }

    fun getRooms(): List<RoomDb> = transaction {
        Rooms.select { Rooms.status eq "on" }
            .orderBy(Rooms.roomId to SortOrder.ASC)
            .map { MapObject.toRoomDb(it) }
    }

    fun getRoomInfos(roomNo: String): List<RoomInfoDb> = transaction {
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
            .map { MapResponse.toRoomInfoDb(it) }
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
                it[dateTime] = System.currentTimeMillis()
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
                .map { MapObject.toRoomNoDb(it) }
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
                it[dateTime] = System.currentTimeMillis()
            }

            RoomInfos.insert {
                it[RoomInfos.roomNo] = roomNo
                it[RoomInfos.playerId] = playerId!!
                it[latitude] = 0.0
                it[longitude] = 0.0
                it[team] = "A"
                it[status] = "unready"
                it[dateTime] = System.currentTimeMillis()
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
                it[dateTime] = System.currentTimeMillis()
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
