package com.adedom.teg.data.repositories

import com.adedom.teg.data.database.*
import com.adedom.teg.data.map.Mapper
import com.adedom.teg.data.models.*
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

internal class ReportRepositoryImpl(
    private val mapper: Mapper,
) : ReportRepository {

    override fun itemCollection(): List<ItemCollectionDb> {
        return transaction {
            ItemCollections.selectAll()
                .orderBy(ItemCollections.dateTime, SortOrder.DESC)
                .map { mapper.itemCollection(it) }
        }
    }

    override fun logActive(): List<LogActiveDb> {
        return transaction {
            LogActives.selectAll()
                .orderBy(LogActives.dateTimeIn, SortOrder.DESC)
                .map { mapper.logActive(it) }
        }
    }

    override fun multiCollection(): List<MultiCollectionDb> {
        return transaction {
            MultiCollections.selectAll()
                .orderBy(MultiCollections.dateTime, SortOrder.DESC)
                .map { mapper.multiCollection(it) }
        }
    }

    override fun multiItem(): List<MultiItemDb> {
        return transaction {
            MultiItems.selectAll()
                .orderBy(MultiItems.dateTimeCreated, SortOrder.DESC)
                .map { mapper.multiItem(it) }
        }
    }

    override fun player(): List<PlayerDb> {
        return transaction {
            Players.selectAll()
                .orderBy(Players.dateTimeCreated, SortOrder.DESC)
                .map { mapper.player(it) }
        }
    }

    override fun room(): List<RoomDb> {
        return transaction {
            Rooms.selectAll()
                .orderBy(Rooms.dateTime, SortOrder.DESC)
                .map { mapper.room(it) }
        }
    }

    override fun roomInfo(): List<RoomInfoDb> {
        return transaction {
            RoomInfos.selectAll()
                .orderBy(RoomInfos.dateTime, SortOrder.DESC)
                .map { mapper.roomInfo(it) }
        }
    }

    override fun singleItem(): List<SingleItemDb> {
        return transaction {
            SingleItems.selectAll()
                .orderBy(SingleItems.dateTimeCreated, SortOrder.DESC)
                .map { mapper.singleItem(it) }
        }
    }

}
