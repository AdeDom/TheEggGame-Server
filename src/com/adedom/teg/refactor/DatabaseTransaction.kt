package com.adedom.teg.refactor

import com.adedom.teg.data.database.Multis
import com.adedom.teg.data.database.RoomInfos
import com.adedom.teg.data.map.MapObject
import com.adedom.teg.data.models.MultiDb
import com.adedom.teg.models.request.MultiRequest
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseTransaction {

    fun validateRoomInfo(roomNo: String): Boolean = transaction {
        val count = RoomInfos.select { RoomInfos.roomNo eq roomNo }
            .count()
            .toInt()

        count == 0
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

}
