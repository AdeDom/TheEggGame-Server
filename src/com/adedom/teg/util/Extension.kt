package com.adedom.teg.util

import com.adedom.teg.data.BASE_URL
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import org.joda.time.DateTime
import java.io.InputStream
import java.io.OutputStream
import java.io.UnsupportedEncodingException
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*

fun DateTime.toDateFormat(): String = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(toDate())

fun Int?.validateAccessToken() = "Please check access token again"

fun String?.validateEmpty() = "Please enter $this"

fun String?.validateLessEqZero() = "Please check the $this again"

fun String?.validateNotFound() = "$this not found"

fun String?.validateGender() = (this == "M" || this == "F")

fun String?.validateTeam() = (this == "A" || this == "B")

fun String?.validateState() = (this == "online" || this == "offline")

fun String?.validateIncorrect() = "$this Incorrect"

fun String?.validateRepeatUsername() = "Username is $this repeat other in the egg game"

fun String?.validateRepeatName() = "Name is $this repeat other in the egg game"

infix fun String?.validateGrateEq(length: Int) = "Please enter a number greater than or equal to $length"

fun String?.encryptSHA(): String {
    var sha = ""
    try {
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val byteArray = messageDigest.digest(this?.toByteArray())
        val bigInteger = BigInteger(1, byteArray)
        sha = bigInteger.toString(16).padStart(64, '0')
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    } catch (e: UnsupportedEncodingException) {
        e.printStackTrace()
    }
    return sha
}

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

fun Int?.toLevel(): Int? = this?.div(1000).let { if (it == 0) 1 else it }

fun String?.toImage(): String? {
    return "${BASE_URL}/api/image/$this"
}
