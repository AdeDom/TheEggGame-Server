package com.adedom.teg.http.controller

import com.adedom.teg.business.report.ReportService
import com.adedom.teg.models.report.ItemCollectionRequest
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

}
