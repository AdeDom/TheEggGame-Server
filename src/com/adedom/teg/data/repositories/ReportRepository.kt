package com.adedom.teg.data.repositories

import com.adedom.teg.data.models.ItemCollectionDb
import com.adedom.teg.data.models.LogActiveDb

internal interface ReportRepository {

    fun itemCollection(): List<ItemCollectionDb>

    fun logActive(): List<LogActiveDb>

}
