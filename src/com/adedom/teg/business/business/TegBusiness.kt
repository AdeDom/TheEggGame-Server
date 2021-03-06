package com.adedom.teg.business.business

import com.adedom.teg.data.models.SingleItemDb
import com.adedom.teg.models.TegLatLng
import com.adedom.teg.models.request.AddSingleItemRequest
import kotlin.reflect.KProperty0

interface TegBusiness {

    fun isValidateGender(gender: String): Boolean

    fun isValidateRankPlayer(rankLimit: Int): Boolean

    fun isValidateDateTime(dateTime: String): Boolean

    fun isValidateState(state: String): Boolean

    fun isValidateJwtIncorrect(token: String, name: String): Boolean

    fun isValidateJwtExpires(token: String): Boolean

    fun isValidateMinUsername(str: String): Boolean

    fun isValidateMinPassword(str: String): Boolean

    fun isValidateLessThanOrEqualToZero(num: Int): Boolean

    fun isMissionMode(mode: String): Boolean

    // old == false, new == true
    fun isValidateDateTimeCurrent(dateTime: Long): Boolean

    fun isValidateMissionSingle(dateTimeList: List<Long>): Boolean

    fun isValidateRoomPeople(people: Int): Boolean

    fun isValidateTeam(team: String): Boolean

    fun isValidatePlayMode(mode: String): Boolean

    fun convertBirthDateStringToLong(birthDate: String): Long

    fun toConvertDateTimeLongToString(date: Long?): String

    fun toConvertLevel(point: Int?): Int

    fun encryptSHA(password: String): String

    fun distanceBetween(startP: TegLatLng, endP: TegLatLng): Double

    fun generateSingleItem(currentLatLng: TegLatLng): AddSingleItemRequest

    fun randomSingleItemCollection(itemTypeId: Int?): Pair<Int, Int>

    fun addSingleItemTimes(currentLatLng: TegLatLng, singleItems: List<SingleItemDb>): Int

    fun generateMultiItem(currentLatLng: TegLatLng): TegLatLng

    fun multiPlayerEndGame(scoreTeamA: Int?, scoreTeamB: Int?, team: String): Triple<String, String, Boolean>?

    fun toMessageIsNullOrBlank(values: String?): String

    fun toMessageIsNullOrBlank(kProperty0: KProperty0<String?>): String

    fun toMessageIsNullOrBlank1(kProperty0: KProperty0<Int?>): String

    fun toMessageIsNullOrBlank2(kProperty0: KProperty0<Double?>): String

    fun toMessageIncorrect(str: String): String

    fun toMessageIncorrect(kProperty0: KProperty0<String?>): String

    fun toMessageIncorrect1(kProperty0: KProperty0<Int?>): String

    fun toMessageGender(kProperty0: KProperty0<String?>): String

    fun toMessageRepeat(kProperty0: KProperty0<String?>, other: String): String

    fun toMessageGrateEq(kProperty0: KProperty0<String?>, length: Int): String

    fun toMessagePeopleRoomInfo(): String

    fun toMessageTegMultiPeople(): String

    fun toMessageTegMultiTeam(): String

    fun toMessageTegMultiStatus(): String

}
