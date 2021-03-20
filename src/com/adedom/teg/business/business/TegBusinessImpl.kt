package com.adedom.teg.business.business

import com.adedom.teg.data.models.SingleItemDb
import com.adedom.teg.models.TegLatLng
import com.adedom.teg.models.request.AddSingleItemRequest
import com.adedom.teg.util.TegConstant
import com.auth0.jwt.JWT
import java.io.UnsupportedEncodingException
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.reflect.KProperty0

class TegBusinessImpl : TegBusiness {

    override fun isValidateGender(gender: String): Boolean {
        return gender == TegConstant.GENDER_MALE || gender == TegConstant.GENDER_FEMALE
    }

    override fun isValidateRankPlayer(rankLimit: Int): Boolean {
        return rankLimit == TegConstant.RANK_LIMIT_TEN ||
                rankLimit == TegConstant.RANK_LIMIT_FIFTY ||
                rankLimit == TegConstant.RANK_LIMIT_HUNDRED
    }

    override fun isValidateDateTime(dateTime: String): Boolean {
        return try {
            val date = SimpleDateFormat("dd/MM/yyyy").parse(dateTime)
            val year = SimpleDateFormat("yyyy").format(date).toInt()
            year !in 1900..2100
        } catch (e: Throwable) {
            true
        }
    }

    override fun isValidateState(state: String): Boolean {
        return state == TegConstant.STATE_ONLINE || state == TegConstant.STATE_OFFLINE
    }

    override fun isValidateJwtIncorrect(token: String, name: String): Boolean = try {
        JWT().decodeJwt(token).getClaim(name).asString() == null
    } catch (e: Throwable) {
        true
    }

    override fun isValidateJwtExpires(token: String): Boolean = try {
        JWT().decodeJwt(token).getClaim("exp").asDate() < Date()
    } catch (e: Throwable) {
        true
    }

    override fun isValidateMinUsername(str: String): Boolean {
        return str.length < TegConstant.MIN_USERNAME
    }

    override fun isValidateMinPassword(str: String): Boolean {
        return str.length < TegConstant.MIN_PASSWORD
    }

    override fun isValidateLessThanOrEqualToZero(num: Int): Boolean {
        return num <= 0
    }

    override fun isMissionMode(mode: String): Boolean {
        return mode == TegConstant.MISSION_DELIVERY || mode == TegConstant.MISSION_SINGLE || mode == TegConstant.MISSION_MULTI
    }

    override fun isValidateDateTimeCurrent(dateTime: Long): Boolean {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val dateTimeLast = Date(dateTime)
        val dateTimeNow = Date(System.currentTimeMillis())
        return sdf.format(dateTimeLast) == sdf.format(dateTimeNow)
    }

    override fun isValidateMissionSingle(dateTimeList: List<Long>): Boolean {
        if (dateTimeList.size < TegConstant.MISSION_SINGLE_QTY) {
            return false
        } else {
            dateTimeList.forEach {
                if (!isValidateDateTimeCurrent(it)) {
                    return false
                }
            }

            return true
        }
    }

    override fun isValidateRoomPeople(people: Int): Boolean {
        return people !in TegConstant.ROOM_PEOPLE_MIN..TegConstant.ROOM_PEOPLE_MAX
    }

    override fun isValidateTeam(team: String): Boolean {
        return team == TegConstant.TEAM_A || team == TegConstant.TEAM_B
    }

    override fun isValidatePlayMode(mode: String): Boolean {
        return mode == TegConstant.PLAY_MODE_MAIN || mode == TegConstant.PLAY_MODE_SINGLE || mode == TegConstant.PLAY_MODE_MULTI
    }

    override fun convertBirthDateStringToLong(birthDate: String): Long {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("en", "EN"))
        return sdf.parse(birthDate).time
    }

    override fun toConvertDateTimeLongToString(date: Long?): String {
        return if (date == null)
            "Error"
        else
            SimpleDateFormat("dd/MM/yyyy", Locale("en", "EN")).format(date)
    }

    override fun toConvertLevel(point: Int?): Int {
        val level = point?.div(1000) ?: 1
        return if (level == 0) 1 else level
    }

    override fun encryptSHA(password: String): String {
        var sha = ""
        try {
            val messageDigest = MessageDigest.getInstance("SHA-256")
            val byteArray = messageDigest.digest(password.toByteArray())
            val bigInteger = BigInteger(1, byteArray)
            sha = bigInteger.toString(16).padStart(64, '0')
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return sha
    }

    override fun distanceBetween(startP: TegLatLng, endP: TegLatLng): Double {
        val lat1: Double = startP.latitude
        val lat2: Double = endP.latitude
        val lon1: Double = startP.longitude
        val lon2: Double = endP.longitude
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * asin(sqrt(a))
        return 6366000 * c
    }

    override fun generateSingleItem(currentLatLng: TegLatLng): AddSingleItemRequest {
        var latitude = 0.0
        var longitude = 0.0
        var distant = 0.0
        while (distant < 200) {
            when ((1..4).random()) {
                1 -> {
                    latitude = currentLatLng.latitude + (((2..10).random()).toDouble() / 1000)
                    longitude = currentLatLng.longitude + (((2..10).random()).toDouble() / 1000)
                }
                2 -> {
                    latitude = currentLatLng.latitude + (((2..10).random()).toDouble() / 1000)
                    longitude = currentLatLng.longitude - (((2..10).random()).toDouble() / 1000)
                }
                3 -> {
                    latitude = currentLatLng.latitude - (((2..10).random()).toDouble() / 1000)
                    longitude = currentLatLng.longitude + (((2..10).random()).toDouble() / 1000)
                }
                4 -> {
                    latitude = currentLatLng.latitude - (((2..10).random()).toDouble() / 1000)
                    longitude = currentLatLng.longitude - (((2..10).random()).toDouble() / 1000)
                }
            }
            distant = distanceBetween(currentLatLng, TegLatLng(latitude, longitude))
        }

        return AddSingleItemRequest(
            itemTypeId = (1..4).random(),
            latitude = latitude,
            longitude = longitude,
        )
    }

    override fun randomSingleItemCollection(itemTypeId: Int?): Pair<Int, Int> {
        val item = listOf(
            TegConstant.SINGLE_ITEM_ONE,
            TegConstant.SINGLE_ITEM_TWO,
            TegConstant.SINGLE_ITEM_THREE,
        )

        var itemId = 0
        var qty = 0

        when (itemTypeId) {
            1 -> {
                itemId = TegConstant.ITEM_LEVEL
                qty = (20..100).random()
            }
            2 -> {
                when ((0..1).random()) {
                    0 -> {
                        itemId = TegConstant.ITEM_LEVEL
                        qty = (50..100).random()
                    }
                    1 -> {
                        itemId = item.random()
                        qty = 1
                    }
                }
            }
            3 -> {
                itemId = item.random()
                qty = (1..3).random()
            }
            4 -> {
                itemId = TegConstant.ITEM_LEVEL
                qty = (300..500).random()
            }
        }

        return Pair(itemId, qty)
    }

    override fun addSingleItemTimes(currentLatLng: TegLatLng, singleItems: List<SingleItemDb>): Int {
        val addSingleItemCount = singleItems
            .map { distanceBetween(currentLatLng, TegLatLng(it.latitude, it.longitude)) }
            .filter { it < 3000 }
            .count()

        return if (addSingleItemCount < 10) 10 - addSingleItemCount else 0
    }

    override fun generateMultiItem(currentLatLng: TegLatLng): TegLatLng {
        var latitude = 0.0
        var longitude = 0.0
        var distant = 0.0
        while (distant < 200) {
            when ((1..4).random()) {
                1 -> {
                    latitude = currentLatLng.latitude + (((2..10).random()).toDouble() / 1000)
                    longitude = currentLatLng.longitude + (((2..10).random()).toDouble() / 1000)
                }
                2 -> {
                    latitude = currentLatLng.latitude + (((2..10).random()).toDouble() / 1000)
                    longitude = currentLatLng.longitude - (((2..10).random()).toDouble() / 1000)
                }
                3 -> {
                    latitude = currentLatLng.latitude - (((2..10).random()).toDouble() / 1000)
                    longitude = currentLatLng.longitude + (((2..10).random()).toDouble() / 1000)
                }
                4 -> {
                    latitude = currentLatLng.latitude - (((2..10).random()).toDouble() / 1000)
                    longitude = currentLatLng.longitude - (((2..10).random()).toDouble() / 1000)
                }
            }
            distant = distanceBetween(currentLatLng, TegLatLng(latitude, longitude))
        }

        return TegLatLng(latitude, longitude)
    }

    override fun multiPlayerEndGame(
        scoreTeamA: Int?,
        scoreTeamB: Int?,
        team: String
    ): Triple<String, String, Boolean>? {
        return when {
            scoreTeamA == scoreTeamB ->
                Triple(
                    TegConstant.MULTI_PLAYER_RESULT_ALWAYS,
                    TegConstant.MULTI_PLAYER_RESULT_ALWAYS,
                    true
                )
            (scoreTeamA ?: 0) > (scoreTeamB ?: 0) ->
                Triple(
                    TegConstant.MULTI_PLAYER_RESULT_WIN,
                    TegConstant.MULTI_PLAYER_RESULT_LOSE,
                    team == TegConstant.TEAM_A
                )
            (scoreTeamA ?: 0) < (scoreTeamB ?: 0) ->
                Triple(
                    TegConstant.MULTI_PLAYER_RESULT_LOSE,
                    TegConstant.MULTI_PLAYER_RESULT_WIN,
                    team == TegConstant.TEAM_B
                )
            else -> null
        }
    }

    override fun convertLongToTimeString(millis: Long): String {
        return when {
            millis in 0..59_999 -> "00:${getSecondTimeString(millis)}"
            millis in 60_000..3_599_999 -> {
                val minutes = millis.div(60_000).toString().padStart(2, '0')
                val second = getSecondTimeString(millis % 60_000).padStart(2, '0')
                "$minutes:$second"
            }
            millis in 3_600_000..86_399_999L -> {
                val hour = millis.div(3_600_000)
                val minuteAndSecond = convertLongToTimeString(millis % 3_600_000)
                "$hour:$minuteAndSecond"
            }
            millis >= 86_400_000L -> {
                val day = millis.div(86_400_000L)
                val time = convertLongToTimeString(millis % 86_400_000L)
                "${day}day $time"
            }
            else -> "Error"
        }
    }

    private fun getSecondTimeString(millis: Long) = when (millis) {
        in 0..59_999 -> millis.div(1_000).toString().padStart(2, '0')
        else -> "Error"
    }

    override fun toMessageIsNullOrBlank(values: String?): String {
        return "Please enter $values"
    }

    override fun toMessageIsNullOrBlank(kProperty0: KProperty0<String?>): String {
        return "Please enter ${kProperty0.name}"
    }

    override fun toMessageIsNullOrBlank1(kProperty0: KProperty0<Int?>): String {
        return "Please enter ${kProperty0.name}"
    }

    override fun toMessageIsNullOrBlank2(kProperty0: KProperty0<Double?>): String {
        return "Please enter ${kProperty0.name}"
    }

    override fun toMessageIncorrect(str: String): String {
        return "$str Incorrect"
    }

    override fun toMessageIncorrect(kProperty0: KProperty0<String?>): String {
        return "${kProperty0.name} Incorrect"
    }

    override fun toMessageIncorrect1(kProperty0: KProperty0<Int?>): String {
        return "${kProperty0.name} Incorrect"
    }

    override fun toMessageGender(kProperty0: KProperty0<String?>): String {
        return "${kProperty0.name} incorrect. Please enter ${TegConstant.GENDER_MALE} or ${TegConstant.GENDER_FEMALE}"
    }

    override fun toMessageRepeat(kProperty0: KProperty0<String?>, other: String): String {
        return "${kProperty0.name} repeat. Please enter other $other"
    }

    override fun toMessageGrateEq(kProperty0: KProperty0<String?>, length: Int): String {
        return "Please enter ${kProperty0.name} a number greater than or equal to $length"
    }

    override fun toMessagePeopleRoomInfo(): String {
        return "People in room is maximum"
    }

    override fun toMessageTegMultiPeople(): String {
        return "A minimum of 2 players is required"
    }

    override fun toMessageTegMultiTeam(): String {
        return "There must be at least 1 player per team"
    }

    override fun toMessageTegMultiStatus(): String {
        return "The players in the room are not ready yet"
    }

}
