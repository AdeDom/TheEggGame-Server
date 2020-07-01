package com.adedom.teg.controller

import com.adedom.teg.request.image.ImageRequest
import com.adedom.teg.util.toResourcesPathName
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.response.respondFile
import io.ktor.routing.Route
import java.io.File

fun Route.imageController() {

    get<ImageRequest> { request ->
        val file = File(request.imageFile.toResourcesPathName())
        if (file.exists()) {
            call.respondFile(file)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }

}
