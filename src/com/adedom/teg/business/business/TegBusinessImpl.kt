package com.adedom.teg.business.business

import com.adedom.teg.util.TegConstant
import com.auth0.jwt.JWT
import java.io.UnsupportedEncodingException
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*
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
            SimpleDateFormat("dd/MM/yyyy").parse(dateTime)
            false
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

    override fun convertBirthdateStringToLong(birthdate: String): Long {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val date = sdf.parse(birthdate)
        return if (date.time < 0L) {
            date.time
        } else {
            val dd = SimpleDateFormat("dd").format(date)
            val MM = SimpleDateFormat("MM").format(date)
            val yyyy = SimpleDateFormat("yyyy").format(date).toInt() - 543
            sdf.parse("$dd/$MM/$yyyy").time
        }
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

    override fun validateGrateEq(kProperty0: KProperty0<String?>, length: Int): String {
        return "Please enter ${kProperty0.name} a number greater than or equal to $length"
    }

}
