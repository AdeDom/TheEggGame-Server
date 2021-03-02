package com.adedom.teg.http.controller

import com.adedom.teg.business.report.ReportService
import com.adedom.teg.models.report.ItemCollectionRequest
import com.adedom.teg.models.report.LogActiveRequest
import com.adedom.teg.models.report.MultiCollectionRequest
import com.adedom.teg.models.report.MultiItemRequest
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

}
