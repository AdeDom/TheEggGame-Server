package com.adedom.teg.route

import com.adedom.teg.request.PutLogActive
import com.adedom.teg.response.BaseResponse
import com.adedom.teg.transaction.DatabaseTransaction
import com.adedom.teg.util.validateEmpty
import com.adedom.teg.util.validateNotFound
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.put
import io.ktor.routing.route

fun Route.putLogActive() {

    route("log-active") {
        put("/") {
            val response = BaseResponse()
            val (logKey) = call.receive<PutLogActive>()
            val message = when {
                logKey.isNullOrBlank() -> PutLogActive::logKey.name.validateEmpty()
                DatabaseTransaction.getCountLogActive(logKey) == 0 -> PutLogActive::logKey.name.validateNotFound()

                else -> {
                    DatabaseTransaction.putLogActive(logKey)
                    response.success = true
                    "Put log active success"
                }
            }
            response.message = message
            call.respond(response)
        }
    }

}
