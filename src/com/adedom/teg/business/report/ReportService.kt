package com.adedom.teg.business.report

import com.adedom.teg.models.report.*

internal interface ReportService {

    fun itemCollection(): ItemCollectionResponse

    fun logActive(): LogActiveResponse

    fun multiCollection(): MultiCollectionResponse

    fun multiItem(): MultiItemResponse

    fun player(): PlayerResponse

    fun room(): RoomResponse

    fun roomInfo(): RoomInfoResponse

    fun singleItem(): SingleItemResponse

    fun gamePlayerRankings(): GamePlayerRankingsResponse

}
