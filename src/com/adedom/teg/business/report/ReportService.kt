package com.adedom.teg.business.report

import com.adedom.teg.models.report.*
import com.adedom.teg.models.report.testfinal.FinalRequest
import com.adedom.teg.models.report.testfinal.FinalResponse
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
internal interface ReportService {

    fun itemCollection(): ItemCollectionResponse

    fun logActive(): LogActiveResponse

    fun multiCollection(): MultiCollectionResponse

    fun multiItem(): MultiItemResponse

    fun player(): PlayerResponse

    fun room(): RoomResponse

    fun roomInfo(): RoomInfoResponse

    fun singleItem(): SingleItemResponse

    fun gamePlayerRankings(gamePlayerRankingsRequest: GamePlayerRankingsRequest): GamePlayerRankingsResponse

    fun testFinalPantip(finalRequest: FinalRequest): FinalResponse

}
