package com.adedom.teg.controller.account

import com.adedom.teg.controller.account.model.*
import com.adedom.teg.data.ApiConstant
import com.adedom.teg.data.BASE_IMAGE
import com.adedom.teg.getHttpClientOkHttp
import com.adedom.teg.response.BaseResponse
import com.adedom.teg.route.GetConstant
import com.adedom.teg.service.account.AccountService
import com.adedom.teg.util.fromJson
import com.adedom.teg.util.jwt.playerId
import io.ktor.application.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.content.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.io.File
import java.util.*

@KtorExperimentalLocationsAPI
fun Route.accountController(service: AccountService) {

    put<ImageProfile> {
        var imageName: String? = null

        try {
            val multiPartData = call.receiveMultipart()
            multiPartData.forEachPart { part ->
                if (part.name == GetConstant.IMAGE_FILE && part is PartData.FileItem) {
                    val extension = File(part.originalFileName!!).extension
                    imageName = "image-${call.playerId}.$extension"

                    val byteArray = part.streamProvider().readBytes()
                    val encodeToString = Base64.getEncoder().encodeToString(byteArray)
                    val response = getHttpClientOkHttp().post<HttpResponse> {
                        url(BASE_IMAGE + "upload-image.php")
                        body = MultiPartFormDataContent(formData {
                            append(ApiConstant.name, imageName!!)
                            append(ApiConstant.image, encodeToString)
                        })
                    }.fromJson<BaseResponse>()

                    if (!response.success) imageName = null
                }
                part.dispose()
            }
        } catch (e: IllegalStateException) {
        }

        val response = service.changeImageProfile(call.playerId, imageName)
        call.respond(response)
    }

    get<PlayerInfoRequest> {
        val response = service.fetchPlayerInfo(call.playerId)
        call.respond(response)
    }

    patch<StateRequest> {
        val response = service.playerState(call.playerId, it)
        call.respond(response)
    }

    put<ChangePasswordRequest> {
        val request = call.receive<ChangePasswordRequest>()
        val response = service.changePassword(call.playerId, request)
        call.respond(response)
    }

    put<ChangeProfileRequest> {
        val request = call.receive<ChangeProfileRequest>()
        val response = service.changeProfile(call.playerId, request)
        call.respond(response)
    }

}
