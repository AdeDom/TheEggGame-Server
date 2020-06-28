package com.adedom.teg.route

import com.adedom.teg.service.TegService
import com.adedom.teg.util.toResourcesPathName
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondFile
import io.ktor.routing.Route
import io.ktor.routing.get
import java.io.File

fun Route.testRoute(service: TegService) {

    get("/{${GetConstant.IMAGE_FILE}}") {
        val imageFile = call.parameters[GetConstant.IMAGE_FILE]!!
        val file = File(imageFile.toResourcesPathName())
        if (file.exists()) {
            call.respondFile(file)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }

}
