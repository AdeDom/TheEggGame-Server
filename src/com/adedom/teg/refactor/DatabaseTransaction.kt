package com.adedom.teg.refactor

import com.adedom.teg.data.database.MultiItems
import com.adedom.teg.data.database.RoomInfos
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

    fun postMulti(multiRequest: MultiRequest) {
        val (roomNo, latitude, longitude) = multiRequest
        transaction {
            MultiItems.insert {
                it[MultiItems.roomNo] = roomNo!!
                it[MultiItems.latitude] = latitude!!
                it[MultiItems.longitude] = longitude!!
                it[status] = "on"
            }
        }
    }

}
