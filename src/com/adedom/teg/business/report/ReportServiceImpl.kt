package com.adedom.teg.business.report

import com.adedom.teg.business.business.TegBusiness
import com.adedom.teg.data.repositories.ReportRepository
import com.adedom.teg.models.report.*
import com.adedom.teg.models.report.testfinal.*
import com.adedom.teg.models.report.two.LogActiveHistory
import com.adedom.teg.models.report.two.LogActiveHistoryData
import com.adedom.teg.models.report.two.LogActiveHistoryRequest
import com.adedom.teg.models.report.two.LogActiveHistoryResponse
import com.adedom.teg.util.TegConstant
import io.ktor.locations.*
import java.text.SimpleDateFormat
import java.util.*

@KtorExperimentalLocationsAPI
internal class ReportServiceImpl(
    private val repository: ReportRepository,
    private val business: TegBusiness,
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

    override fun room(): RoomResponse {
        val response = RoomResponse()

        response.success = true
        response.message = "Fetch room success"
        response.rooms = repository.room().map {
            Room(
                roomId = it.roomId,
                roomNo = it.roomNo,
                name = it.name,
                people = it.people,
                status = it.status,
                startTime = it.startTime,
                endTime = it.endTime,
                dateTime = it.dateTime,
            )
        }

        return response
    }

    override fun roomInfo(): RoomInfoResponse {
        val response = RoomInfoResponse()

        response.success = true
        response.message = "Fetch room info success"
        response.roomInfoList = repository.roomInfo().map {
            RoomInfo(
                infoId = it.infoId,
                roomNo = it.roomNo,
                playerId = it.playerId,
                latitude = it.latitude,
                longitude = it.longitude,
                team = it.team,
                status = it.status,
                role = it.role,
                dateTime = it.dateTime,
            )
        }

        return response
    }

    override fun singleItem(): SingleItemResponse {
        val response = SingleItemResponse()

        response.success = true
        response.message = "Fetch single item success"
        response.singleItems = repository.singleItem().map {
            SingleItem(
                singleId = it.singleId,
                itemTypeId = it.itemTypeId,
                latitude = it.latitude,
                longitude = it.longitude,
                playerId = it.playerId,
                status = it.status,
                dateTimeCreated = it.dateTimeCreated,
                dateTimeUpdated = it.dateTimeUpdated,
            )
        }

        return response
    }

    override fun gamePlayerRankings(gamePlayerRankingsRequest: GamePlayerRankingsRequest): GamePlayerRankingsResponse {
        val response = GamePlayerRankingsResponse()
        val (_, begin, end) = gamePlayerRankingsRequest

        val message: String = when {
            else -> {
                val getPlayerDb = repository.player()
                val getItemCollectionDb = repository.itemCollection()

                var gamePlayerRankings = getPlayerDb.map { db ->
                    val level = getItemCollectionDb
                        .filter { it.playerId == db.playerId && it.itemId == TegConstant.ITEM_LEVEL }
                        .sumBy { it.qty }

                    GamePlayerRanking(
                        playerId = db.playerId,
                        name = db.name,
                        image = db.image,
                        gender = db.gender,
                        birthDateLong = db.birthDate,
                        birthDateString = business.toConvertDateTimeLongToString(db.birthDate),
                        state = db.state,
                        latitude = db.latitude,
                        longitude = db.longitude,
                        currentMode = db.currentMode,
                        dateTimeCreated = business.toConvertDateTimeLongToString(db.dateTimeCreated),
                        dateTimeUpdated = business.toConvertDateTimeLongToString(db.dateTimeUpdated),
                        level = business.toConvertLevel(level),
                    )
                }.sortedByDescending { it.level }

                if (begin != null && end != null) {
                    gamePlayerRankings = gamePlayerRankings.filter { it.level in (begin..end) }
                }

                val genderCount = GenderCount(
                    male = gamePlayerRankings.filter { it.gender == TegConstant.GENDER_MALE }.size,
                    female = gamePlayerRankings.filter { it.gender == TegConstant.GENDER_FEMALE }.size,
                )
                val stateCount = StateCount(
                    online = gamePlayerRankings.filter { it.state == TegConstant.STATE_ONLINE }.size,
                    offline = gamePlayerRankings.filter { it.state == TegConstant.STATE_OFFLINE }.size,
                )
                val modeCount = ModeCount(
                    main = gamePlayerRankings.filter { it.currentMode == TegConstant.PLAY_MODE_MAIN }.size,
                    single = gamePlayerRankings.filter { it.currentMode == TegConstant.PLAY_MODE_SINGLE }.size,
                    multi = gamePlayerRankings.filter { it.currentMode == TegConstant.PLAY_MODE_MULTI }.size,
                )

                response.peopleAll = gamePlayerRankings.size
                response.genderCount = genderCount
                response.stateCount = stateCount
                response.modeCount = modeCount
                response.gamePlayerRankings = gamePlayerRankings
                response.success = true
                "Fetch game player rankings success"
            }
        }

        response.message = message
        return response
    }

    override fun logActiveHistory(logActiveHistoryRequest: LogActiveHistoryRequest): LogActiveHistoryResponse {
        val response = LogActiveHistoryResponse()

        val logActive = repository.logActiveHistory(logActiveHistoryRequest)
        val player = repository.player()

        var grandTotalTimePeriodLong = 0L
        logActive.forEach {
            if (it.dateTimeOut != null) {
                grandTotalTimePeriodLong += it.dateTimeOut - it.dateTimeIn
            }
        }

        val logActiveHistories = mutableListOf<LogActiveHistory>()
        logActive
            .distinctBy { it.playerId }
            .forEach { playerScope ->
                // logActiveHistoryDataList
                val logActiveHistoryDataList = mutableListOf<LogActiveHistoryData>()
                logActive
                    .filter { it.playerId == playerScope.playerId }
                    .forEach { laScope ->
                        val dateSdf = SimpleDateFormat("dd/MM/yy")
                        val timeSdf = SimpleDateFormat("HH:mm:ss")

                        val logActiveHistoryData = LogActiveHistoryData(
                            dataId = UUID.randomUUID().toString().replace("-", ""),
                            dateIn = dateSdf.format(laScope.dateTimeIn),
                            timeIn = timeSdf.format(laScope.dateTimeIn),
                            dateOut = if (laScope.dateTimeOut == null) "" else dateSdf.format(laScope.dateTimeOut),
                            timeOut = if (laScope.dateTimeOut == null) "" else timeSdf.format(laScope.dateTimeOut),
                            timePeriod = if (laScope.dateTimeOut == null) {
                                ""
                            } else {
                                business.convertLongToTimeString(laScope.dateTimeOut - laScope.dateTimeIn)
                            },
                        )
                        logActiveHistoryDataList.add(logActiveHistoryData)
                    }

                // totalTimePeriod
                var totalTimePeriodLong = 0L
                logActive
                    .filter { it.playerId == playerScope.playerId }
                    .forEach {
                        if (it.dateTimeOut != null) {
                            totalTimePeriodLong += it.dateTimeOut - it.dateTimeIn
                        }
                    }

                // LogActiveHistory
                val logActiveHistoryItem = LogActiveHistory(
                    playerId = playerScope.playerId,
                    name = player.single { it.playerId == playerScope.playerId }.name,
                    time = logActive.filter { it.playerId == playerScope.playerId }.size,
                    totalTimePeriod = business.convertLongToTimeString(totalTimePeriodLong),
                    logActiveHistoryDataList = logActiveHistoryDataList,
                )
                logActiveHistories.add(logActiveHistoryItem)
            }

        response.peopleAll = logActive.distinctBy { it.playerId }.size
        response.grandTotalTimePeriod = business.convertLongToTimeString(grandTotalTimePeriodLong)
        response.logActiveHistories = logActiveHistories

        return response
    }

    override fun testFinalPantip(finalRequest: FinalRequest): FinalResponse {
        val response = FinalResponse()

        val testFinalPantips = repository.testFinalPantips(finalRequest).map {
            TestFinalPantipService(
                collectionId = it.collectionId,
                playerId = it.playerId,
                itemId = it.itemId,
                qty = it.qty,
                dateTimeLong = it.dateTime,
                dateString = SimpleDateFormat("d/M/yy").format(it.dateTime),
                timeString = SimpleDateFormat("HH:mm").format(it.dateTime),
                mode = it.mode,
            )
        }
        val player = repository.player()

        val finals = mutableListOf<Final>()
        testFinalPantips
            .distinctBy { it.playerId }
            .forEach { playerScope ->

                val dataList = mutableListOf<Data>()
                testFinalPantips
                    .filter { it.playerId == playerScope.playerId }
                    .distinctBy { it.dateString }
                    .forEach { dateScope ->
                        val subDataList = mutableListOf<SubData>()
                        testFinalPantips
                            .filter { it.playerId == playerScope.playerId }
                            .filter { it.dateString == dateScope.dateString }
                            .distinctBy { it.timeString }
                            .forEach { timeScope ->
                                val list = testFinalPantips
                                    .filter { it.playerId == playerScope.playerId }
                                    .filter { it.dateString == dateScope.dateString }
                                    .filter { it.timeString == timeScope.timeString }

                                val subData = SubData(
                                    subDataId = UUID.randomUUID().toString().replace("-", ""),
                                    date = timeScope.dateString,
                                    time = timeScope.timeString,
                                    itemA = list.filter { it.itemId == TegConstant.SINGLE_ITEM_ONE }
                                        .sumBy { it.qty },
                                    itemB = list.filter { it.itemId == TegConstant.SINGLE_ITEM_TWO }
                                        .sumBy { it.qty },
                                    itemC = list.filter { it.itemId == TegConstant.SINGLE_ITEM_THREE }
                                        .sumBy { it.qty },
                                    itemD = list.filter { it.itemId == TegConstant.ITEM_LEVEL }
                                        .sumBy { it.qty },
                                    totalScore = list.sumBy { it.qty },
                                )
                                subDataList.add(subData)
                            }

                        val data = Data(
                            dataId = UUID.randomUUID().toString().replace("-", ""),
                            branchTotalScore = testFinalPantips
                                .filter { it.playerId == playerScope.playerId }
                                .filter { it.dateString == dateScope.dateString }
                                .sumBy { it.qty },
                            subData = subDataList,
                        )
                        dataList.add(data)
                    }

                val final = Final(
                    playerId = playerScope.playerId,
                    name = player.single { it.playerId == playerScope.playerId }.name,
                    totalDate = testFinalPantips
                        .filter { it.playerId == playerScope.playerId }
                        .distinctBy { it.dateString }
                        .size,
                    subTotalScore = testFinalPantips
                        .filter { it.playerId == playerScope.playerId }
                        .sumBy { it.qty },
                    `data` = dataList,
                )
                finals.add(final)
            }

        response.success = true
        response.message = "Fetch test final pantip success"
        response.grandTotalPeople = testFinalPantips.distinctBy { it.playerId }.size
        response.grandTotalScore = testFinalPantips.sumBy { it.qty }
        response.finals = finals

        return response
    }

}
