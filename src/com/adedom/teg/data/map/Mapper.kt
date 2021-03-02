package com.adedom.teg.data.map

import com.adedom.teg.data.models.ItemCollectionDb
import com.adedom.teg.data.models.LogActiveDb
import org.jetbrains.exposed.sql.ResultRow

internal interface Mapper {

    fun itemCollection(row: ResultRow): ItemCollectionDb

    fun logActive(row: ResultRow): LogActiveDb

}
