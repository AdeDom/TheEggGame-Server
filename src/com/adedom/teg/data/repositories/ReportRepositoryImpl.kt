package com.adedom.teg.data.repositories

import com.adedom.teg.data.database.ItemCollections
import com.adedom.teg.data.database.LogActives
import com.adedom.teg.data.database.MultiCollections
import com.adedom.teg.data.map.Mapper
import com.adedom.teg.data.models.ItemCollectionDb
import com.adedom.teg.data.models.LogActiveDb
import com.adedom.teg.data.models.MultiCollectionDb
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

}
