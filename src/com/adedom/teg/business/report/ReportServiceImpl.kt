package com.adedom.teg.business.report

import com.adedom.teg.models.report.ItemCollection
import com.adedom.teg.models.report.ItemCollectionResponse

internal class ReportServiceImpl : ReportService {

    override fun itemCollection(): ItemCollectionResponse {
        val response = ItemCollectionResponse()

        response.success = true
        response.message = "Fetch item collection success"
        response.itemCollections = listOf(
            ItemCollection(
                collectionId = 1,
                playerId = "2",
                itemId = 3,
                qty = 4,
                latitude = 5.0,
                longitude = 6.0,
                dateTime = 7,
                mode = "8",
            ),
            ItemCollection(
                collectionId = 11,
                playerId = "22",
                itemId = 33,
                qty = 44,
                latitude = 55.0,
                longitude = 66.0,
                dateTime = 77,
                mode = "88",
            )
        )

        return response
    }

}
