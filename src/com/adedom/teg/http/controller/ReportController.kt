package com.adedom.teg.http.controller

import com.adedom.teg.business.report.ReportService
import com.adedom.teg.models.report.ItemCollectionRequest
import com.adedom.teg.models.report.LogActiveRequest
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

}
