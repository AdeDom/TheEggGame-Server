package com.adedom.teg.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import org.joda.time.DateTime
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

fun DateTime.toDateFormat(): String = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(toDate())

fun String?.validateAccessToken() = "Please check access token = [$this] again"

fun Int?.validateFlagLogActive() = (this == 0 || this == 1)

fun String?.validateIsNullOrBlank() = "Please enter $this"

fun String?.validateLessEqZero() = "Please check the $this again"

fun String?.validateNotFound() = "$this not found"

fun String?.validateGender() = (this == "M" || this == "F")

// if parse success return false
fun String?.isValidateDateTime(): Boolean {
    return try {
        SimpleDateFormat("dd/MM/yyyy").parse(this)
        false
    } catch (e: Throwable) {
        true
    }
}

fun String?.toMessageIncorrect() = "$this Incorrect"

fun String?.validateTeam() = (this == "A" || this == "B")

fun String?.validateState() = (this == "online" || this == "offline")

fun String?.validateIncorrect() = "$this Incorrect"

fun String?.toMessageGender() = "$this incorrect. Please enter M or F"

fun String?.toMessageRepeat(other: String) = "$this repeat. Please enter other $other"

fun String?.validateRepeatUsername() = "Username is $this repeat other in the egg game"

fun String?.validateRepeatName() = "Name is $this repeat other in the egg game"

infix fun String?.validateGrateEq(length: Int) = "Please enter $this a number greater than or equal to $length"

suspend fun InputStream.copyToSuspend(
    out: OutputStream,
    bufferSize: Int = DEFAULT_BUFFER_SIZE,
    yieldSize: Int = 4 * 1024 * 1024,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
): Long {
    return withContext(dispatcher) {
        val buffer = ByteArray(bufferSize)
        var bytesCopied = 0L
        var bytesAfterYield = 0L
        while (true) {
            val bytes = read(buffer).takeIf { it >= 0 } ?: break
            out.write(buffer, 0, bytes)
            if (bytesAfterYield >= yieldSize) {
                yield()
                bytesAfterYield %= yieldSize
            }
            bytesCopied += bytes
            bytesAfterYield += bytes
        }
        return@withContext bytesCopied
    }
}

fun String.toResourcesPathName(): String = "teg-file/$this"

fun Int?.toLevel(): Int = this?.div(1000) ?: 1

fun Long?.toConvertBirthdate(): String = SimpleDateFormat("dd/MM/yyyy", Locale("th", "TH")).format(this)
