package com.adedom.teg.http.controller

import com.adedom.teg.business.report.ReportService
import com.adedom.teg.models.report.*
import com.adedom.teg.models.report.five.MultiCollectionHistoryRequest
import com.adedom.teg.models.report.four.ItemCollectionHistoryRequest
import com.adedom.teg.models.report.six.SingleItemHistoryRequest
import com.adedom.teg.models.report.testfinal.FinalRequest
import com.adedom.teg.models.report.three.RoomHistoryRequest
import com.adedom.teg.models.report.two.LogActiveHistoryRequest
import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*

@KtorExperimentalLocationsAPI
internal fun Route.reportController(service: ReportService) {

    get<ItemCollectionRequest> {
        val response = service.itemCollection()
        call.respond(response)
    }

    get<LogActiveRequest> {
        val response = service.logActive()
        call.respond(response)
    }

    get<MultiCollectionRequest> {
        val response = service.multiCollection()
        call.respond(response)
    }

    get<MultiItemRequest> {
        val response = service.multiItem()
        call.respond(response)
    }

    get<PlayerRequest> {
        val response = service.player()
        call.respond(response)
    }

    get<RoomRequest> {
        val response = service.room()
        call.respond(response)
    }

    get<RoomInfoRequest> {
        val response = service.roomInfo()
        call.respond(response)
    }

    get<SingleItemRequest> {
        val response = service.singleItem()
        call.respond(response)
    }

    get<GamePlayerRankingsRequest> { request ->
        val response = service.gamePlayerRankings(request)
        call.respond(response)
    }

    get<LogActiveHistoryRequest> { request ->
        val response = service.logActiveHistory(request)
        call.respond(response)
    }

    get<RoomHistoryRequest> { request ->
        val response = service.roomHistory(request)
        call.respond(response)
    }

    get<ItemCollectionHistoryRequest> { request ->
        val response = service.itemCollectionHistory(request)
        call.respond(response)
    }

    get<MultiCollectionHistoryRequest> { request ->
        val response = service.multiCollectionHistory(request)
        call.respond(response)
    }

    get<SingleItemHistoryRequest> { request ->
        val response = service.singleItemHistory(request)
        call.respond(response)
    }

    get<FinalRequest> { request ->
        val response = service.testFinalPantip(request)
        call.respond(response)
    }

}
