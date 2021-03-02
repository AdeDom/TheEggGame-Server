package com.adedom.teg.data.repositories

import com.adedom.teg.data.models.ItemCollectionDb

internal interface ReportRepository {

    fun itemCollection(): List<ItemCollectionDb>

}
