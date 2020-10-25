package com.adedom.teg.business.application

import com.adedom.teg.business.business.TegBusiness
import com.adedom.teg.data.repositories.TegRepository
import com.adedom.teg.models.request.MissionRequest
import com.adedom.teg.models.request.RankPlayersRequest
import com.adedom.teg.models.response.*
import com.adedom.teg.util.TegConstant
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
class ApplicationServiceImpl(
    private val repository: TegRepository,
    private val business: TegBusiness,
) : ApplicationService {

    override fun fetchRankPlayers(rankPlayersRequest: RankPlayersRequest): RankPlayersResponse {
        val response = RankPlayersResponse()
        val (_, search, limit) = rankPlayersRequest

        val message: String = when {
            // validate Null Or Blank
            search == null -> business.toMessageIsNullOrBlank(rankPlayersRequest::search)
            limit.isNullOrBlank() -> business.toMessageIsNullOrBlank(rankPlayersRequest::limit)
            limit.toIntOrNull() == null -> business.toMessageIsNullOrBlank(rankPlayersRequest::limit)

            // validate values of variable
            business.isValidateLessThanOrEqualToZero(limit.toInt()) -> business.toMessageIncorrect(rankPlayersRequest::limit)
            !business.isValidateRankPlayer(limit.toInt()) -> business.toMessageIncorrect(rankPlayersRequest::limit)

            // validate database

            // execute
            else -> {
                val rankPlayer = repository.fetchRankPlayers(rankPlayersRequest).map {
                    PlayerInfo(
                        playerId = it.playerId,
                        username = it.username,
                        name = it.name?.capitalize(),
                        image = it.image,
                        level = business.toConvertLevel(it.level),
                        state = it.state,
                        gender = it.gender,
                        birthDate = business.toConvertBirthDate(it.birthDate),
                    )
                }
                response.success = true
                response.rankPlayers = rankPlayer
                "Fetch rank players success Hey"
            }
        }

        response.message = message
        return response
    }

    override fun logActiveOn(playerId: String?): BaseResponse {
        val response = BaseResponse()

        val message: String = when {
            // validate Null Or Blank
            playerId.isNullOrBlank() -> business.toMessageIsNullOrBlank(playerId)

            // validate values of variable

            // validate database

            // execute
            else -> {
                response.success = repository.logActiveOn(playerId)
                "Post log active success"
            }
        }

        response.message = message
        return response
    }

    override fun logActiveOff(playerId: String?): BaseResponse {
        val response = BaseResponse()

        val message: String = when {
            // validate Null Or Blank
            playerId.isNullOrBlank() -> business.toMessageIsNullOrBlank(playerId)

            // validate values of variable

            // validate database

            // execute
            else -> {
                response.success = repository.logActiveOff(playerId)
                "Patch log active success"
            }
        }

        response.message = message
        return response
    }

    override fun fetchMissionMain(playerId: String?): MissionResponse {
        val response = MissionResponse()

        val message: String = when {
            // validate Null Or Blank
            playerId.isNullOrBlank() -> business.toMessageIsNullOrBlank(playerId)

            // validate values of variable

            // validate database

            // execute
            else -> {
                // delivery
                val dateTimeDelivery = repository.getMissionDateTimeLast(playerId, TegConstant.MISSION_DELIVERY)

                // single
                val dateTimeSingle = repository.getMissionDateTimeLast(playerId, TegConstant.MISSION_SINGLE)
                val isEggII: Boolean = if (!business.isValidateDateTimeCurrent(dateTimeSingle)) {
                    business.isValidateMissionSingle(repository.fetchMissionSingle(playerId))
                } else {
                    false
                }

                response.missionInfo = MissionInfo(
                    isEggI = !business.isValidateDateTimeCurrent(dateTimeDelivery),
                    isEggII = isEggII,
                )
                response.success = true
                "Fetch mission info success"
            }
        }

        response.message = message
        return response
    }

    override fun missionMain(playerId: String?, missionRequest: MissionRequest): BaseResponse {
        val response = BaseResponse()
        val (mode) = missionRequest

        val message: String = when {
            // validate Null Or Blank
            playerId.isNullOrBlank() -> business.toMessageIsNullOrBlank(playerId)
            mode.isNullOrBlank() -> business.toMessageIsNullOrBlank(missionRequest::mode)

            // validate values of variable
            !business.isMissionMode(mode) -> business.toMessageIncorrect(missionRequest::mode)

            // validate database
            business.isValidateDateTimeCurrent(repository.getMissionDateTimeLast(playerId, mode)) ->
                business.toMessageRepeat(missionRequest::mode, mode)

            // execute
            else -> {
                response.success = repository.missionMain(playerId, missionRequest)
                "Post mission success"
            }
        }

        response.message = message
        return response
    }

}
