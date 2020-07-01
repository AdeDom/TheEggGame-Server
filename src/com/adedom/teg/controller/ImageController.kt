package com.adedom.teg.controller

import com.adedom.teg.route.GetConstant
import com.adedom.teg.util.toResourcesPathName
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondFile
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import java.io.File

fun Route.imageController() {

    route("image") {
        imageRoute()
    }

}

fun Route.imageRoute() {

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
