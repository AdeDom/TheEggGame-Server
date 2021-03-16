package com.adedom.teg.data.repositories

import com.adedom.teg.data.models.*
import com.adedom.teg.models.report.testfinal.FinalRequest
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
internal interface ReportRepository {

    fun itemCollection(): List<ItemCollectionDb>

    fun logActive(): List<LogActiveDb>

    fun multiCollection(): List<MultiCollectionDb>

    fun multiItem(): List<MultiItemDb>

    fun player(): List<PlayerDb>

    fun room(): List<RoomDb>

    fun roomInfo(): List<RoomInfoDb>

    fun singleItem(): List<SingleItemDb>

    fun testFinalPantips(finalRequest: FinalRequest): List<ItemCollectionDb>

}
