package com.adedom.teg.business.report

import com.adedom.teg.data.repositories.ReportRepository
import com.adedom.teg.models.report.*

internal class ReportServiceImpl(
    private val repository: ReportRepository,
) : ReportService {

    override fun itemCollection(): ItemCollectionResponse {
        val response = ItemCollectionResponse()

        response.success = true
        response.message = "Fetch item collection success"
        response.itemCollections = repository.itemCollection().map {
            ItemCollection(
                collectionId = it.collectionId,
                playerId = it.playerId,
                itemId = it.itemId,
                qty = it.qty,
                latitude = it.latitude,
                longitude = it.longitude,
                dateTime = it.dateTime,
                mode = it.mode,
            )
        }

        return response
    }

    override fun logActive(): LogActiveResponse {
        val response = LogActiveResponse()

        response.success = true
        response.message = "Fetch log active success"
        response.logActives = repository.logActive().map {
            LogActive(
                logId = it.logId,
                playerId = it.playerId,
                dateTimeIn = it.dateTimeIn,
                dateTimeOut = it.dateTimeOut,
            )
        }

        return response
    }

    override fun multiCollection(): MultiCollectionResponse {
        val response = MultiCollectionResponse()

        response.success = true
        response.message = "Fetch multi collection success"
        response.multiCollections = repository.multiCollection().map {
            MultiCollection(
                collectionId = it.collectionId,
                roomNo = it.roomNo,
                playerId = it.playerId,
                team = it.team,
                latitude = it.latitude,
                longitude = it.longitude,
                dateTime = it.dateTime,
            )
        }

        return response
    }

    override fun multiItem(): MultiItemResponse {
        val response = MultiItemResponse()

        response.success = true
        response.message = "Fetch multi item success"
        response.multiItems = repository.multiItem().map {
            MultiItem(
                multiId = it.multiId,
                roomNo = it.roomNo,
                latitude = it.latitude,
                longitude = it.longitude,
                status = it.status,
                dateTimeCreated = it.dateTimeCreated,
                dateTimeUpdated = it.dateTimeUpdated,
            )
        }

        return response
    }

    override fun player(): PlayerResponse {
        val response = PlayerResponse()

        response.success = true
        response.message = "Fetch player success"
        response.players = repository.player().map {
            Player(
                playerId = it.playerId,
                username = it.username,
                password = it.password,
                name = it.name,
                image = it.image,
                gender = it.gender,
                birthDate = it.birthDate,
                state = it.state,
                latitude = it.latitude,
                longitude = it.longitude,
                currentMode = it.currentMode,
                dateTimeCreated = it.dateTimeCreated,
                dateTimeUpdated = it.dateTimeUpdated,
            )
        }

        return response
    }

}