package com.adedom.teg.data.repositories

import com.adedom.teg.data.database.*
import com.adedom.teg.data.map.Mapper
import com.adedom.teg.data.models.*
import com.adedom.teg.models.report.testfinal.FinalRequest
import com.adedom.teg.models.report.two.LogActiveHistoryRequest
import io.ktor.locations.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

@KtorExperimentalLocationsAPI
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

    override fun logActiveHistory(logActiveHistoryRequest: LogActiveHistoryRequest): List<LogActiveDb> {
        val (_, dateTimeIn, dateTimeOut) = logActiveHistoryRequest

        return transaction {
            addLogger(StdOutSqlLogger)

            if (dateTimeIn == null && dateTimeOut == null) {
                LogActives.selectAll()
                    .orderBy(LogActives.dateTimeIn, SortOrder.DESC)
                    .map { mapper.logActive(it) }
            } else {
                LogActives.selectAll()
                    .orWhere { LogActives.dateTimeIn.between(dateTimeIn, dateTimeOut) }
                    .orWhere { LogActives.dateTimeOut.between(dateTimeIn, dateTimeOut) }
                    .orderBy(LogActives.dateTimeIn, SortOrder.DESC)
                    .map { mapper.logActive(it) }
            }
        }
    }

    override fun testFinalPantips(finalRequest: FinalRequest): List<ItemCollectionDb> {
        val (_, begin, end) = finalRequest

        return transaction {
            addLogger(StdOutSqlLogger)

            if (begin == null && end == null) {
                (ItemCollections innerJoin Players)
                    .slice(
                        ItemCollections.collectionId,
                        ItemCollections.playerId,
                        ItemCollections.itemId,
                        ItemCollections.qty,
                        ItemCollections.latitude,
                        ItemCollections.longitude,
                        ItemCollections.dateTime,
                        ItemCollections.mode,
                    )
                    .selectAll()
                    .orderBy(ItemCollections.dateTime)
                    .orderBy(Players.dateTimeCreated)
                    .map { mapper.itemCollection(it) }
            } else {
                (ItemCollections innerJoin Players)
                    .slice(
                        ItemCollections.collectionId,
                        ItemCollections.playerId,
                        ItemCollections.itemId,
                        ItemCollections.qty,
                        ItemCollections.latitude,
                        ItemCollections.longitude,
                        ItemCollections.dateTime,
                        ItemCollections.mode,
                    )
                    .select { ItemCollections.dateTime.between(begin, end) }
                    .orderBy(ItemCollections.dateTime)
                    .orderBy(Players.dateTimeCreated)
                    .map { mapper.itemCollection(it) }
            }
        }
    }

}
