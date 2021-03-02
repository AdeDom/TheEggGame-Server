package com.adedom.teg.data.repositories

import com.adedom.teg.data.database.*
import com.adedom.teg.data.map.Mapper
import com.adedom.teg.data.models.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

internal class ReportRepositoryImpl(
    private val mapper: Mapper,
) : ReportRepository {

    override fun itemCollection(): List<ItemCollectionDb> {
        return transaction {
            ItemCollections.selectAll()
                .map { mapper.itemCollection(it) }
        }
    }

    override fun logActive(): List<LogActiveDb> {
        return transaction {
            LogActives.selectAll()
                .map { mapper.logActive(it) }
        }
    }

    override fun multiCollection(): List<MultiCollectionDb> {
        return transaction {
            MultiCollections.selectAll()
                .map { mapper.multiCollection(it) }
        }
    }

    override fun multiItem(): List<MultiItemDb> {
        return transaction {
            MultiItems.selectAll()
                .map { mapper.multiItem(it) }
        }
    }

    override fun player(): List<PlayerDb> {
        return transaction {
            Players.selectAll()
                .map { mapper.player(it) }
        }
    }

    override fun room(): List<RoomDb> {
        return transaction {
            Rooms.selectAll()
                .map { mapper.room(it) }
        }
    }

    override fun roomInfo(): List<RoomInfoDb> {
        return transaction {
            RoomInfos.selectAll()
                .map { mapper.roomInfo(it) }
        }
    }

}
