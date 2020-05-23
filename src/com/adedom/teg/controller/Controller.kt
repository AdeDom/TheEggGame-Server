package com.adedom.teg.controller

import com.adedom.teg.route.fetchList
import com.adedom.teg.route.user
import io.ktor.routing.Route
import io.ktor.routing.route

fun Route.controller() {

    route("/account") {
        user()
    }

    route("fetch-list") {
        fetchList()
    }

}
