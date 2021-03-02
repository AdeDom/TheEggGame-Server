package com.adedom.teg.business.report

import com.adedom.teg.data.repositories.ReportRepository
import com.adedom.teg.models.report.ItemCollection
import com.adedom.teg.models.report.ItemCollectionResponse

internal class ReportServiceImpl(
    private val repository: ReportRepository,
) : ReportService {

    override fun itemCollection(): ItemCollectionResponse {
        val response = ItemCollectionResponse()

        response.success = true
        response.message = "Fetch item collection success"
        response.itemCollections = repository.itemCollection().map {
            ItemCollection(
                collectionId = it.collectionId,
                playerId = it.playerId,
                itemId = it.itemId,
                qty = it.qty,
                latitude = it.latitude,
                longitude = it.longitude,
                dateTime = it.dateTime,
                mode = it.mode,
            )
        }

        return response
    }

}
