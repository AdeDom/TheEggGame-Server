package com.adedom.teg.refactor

import io.ktor.routing.*

fun Route.headerController() {

    route("multi") {
        multi()
    }

}
