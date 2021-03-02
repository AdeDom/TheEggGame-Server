package com.adedom.teg.data.map

import com.adedom.teg.data.models.*
import org.jetbrains.exposed.sql.ResultRow

internal interface Mapper {

    fun itemCollection(row: ResultRow): ItemCollectionDb

    fun logActive(row: ResultRow): LogActiveDb

    fun multiCollection(row: ResultRow): MultiCollectionDb

    fun multiItem(row: ResultRow): MultiItemDb

    fun player(row: ResultRow): PlayerDb

    fun room(row: ResultRow): RoomDb

    fun roomInfo(row: ResultRow): RoomInfoDb

}
