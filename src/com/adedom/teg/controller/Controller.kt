package com.adedom.teg.controller

import com.adedom.teg.route.*
import io.ktor.routing.Route
import io.ktor.routing.route

fun Route.controller() {

    route("account") {
        user()
    }

    route("fetch-list") {
        fetchList()
    }

    route("multi") {
        setLatlng()
        setReady()
        setRoomOff()
        setTeam()
    }

}
