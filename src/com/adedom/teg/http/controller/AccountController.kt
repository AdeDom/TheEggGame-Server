package com.adedom.teg.http.controller

import com.adedom.teg.http.constant.ApiConstant
import com.adedom.teg.getHttpClientOkHttp
import com.adedom.teg.models.request.*
import com.adedom.teg.models.response.BaseResponse
import com.adedom.teg.business.service.account.AccountService
import com.adedom.teg.util.TegConstant
import com.adedom.teg.util.fromJson
import com.adedom.teg.util.playerId
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

    put<ImageProfileRequest> {
        var imageName: String? = null
        var imageImage: String? = null

        try {
            val multiPartData = call.receiveMultipart()
            multiPartData.forEachPart { part ->
                if (part.name == ApiConstant.IMAGE_FILE && part is PartData.FileItem) {
                    val extension = File(part.originalFileName!!).extension
                    imageName = "image-${call.playerId}.$extension"

                    val byteArray = part.streamProvider().readBytes()
                    imageImage = Base64.getEncoder().encodeToString(byteArray)
                }
                part.dispose()
            }
        } catch (e: IllegalStateException) {
        }

        if (imageName != null && imageImage != null) {
            val res = getHttpClientOkHttp().post<HttpResponse> {
                url(TegConstant.BASE_IMAGE + "upload-image.php")
                body = MultiPartFormDataContent(formData {
                    append(ApiConstant.NAME, imageName!!)
                    append(ApiConstant.IMAGE, imageImage!!)
                })
            }.fromJson<BaseResponse>()
            if (!res.success) imageName = null
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
