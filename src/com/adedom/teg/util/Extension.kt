package com.adedom.teg.util

import com.google.gson.Gson
import io.ktor.client.statement.*
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*

fun DateTime.toDateFormat(): String = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(toDate())

fun String?.validateIsNullOrBlank() = "Please enter $this"

fun String?.toMessageIsNullOrBlank() = "Please enter $this"

fun String?.validateLessEqZero() = "Please check the $this again"

fun String?.validateNotFound() = "$this not found"

fun String?.toMessageIncorrect() = "$this Incorrect"

fun String?.validateTeam() = (this == TegConstant.TEAM_A || this == TegConstant.TEAM_B)

fun String?.validateIncorrect() = "$this Incorrect"

fun String?.toMessageGender() =
    "$this incorrect. Please enter ${TegConstant.GENDER_MALE} or ${TegConstant.GENDER_FEMALE}"

fun String?.toMessageRepeat(other: String) = "$this repeat. Please enter other $other"

infix fun String?.validateGrateEq(length: Int) = "Please enter $this a number greater than or equal to $length"

fun Int?.toLevel(): Int = this?.div(1000) ?: 1

fun Long?.toConvertBirthdate(): String = SimpleDateFormat("dd/MM/yyyy", Locale("th", "TH")).format(this)

suspend inline fun <reified T> HttpResponse.fromJson(): T = Gson().fromJson(this.readText(), T::class.java)
