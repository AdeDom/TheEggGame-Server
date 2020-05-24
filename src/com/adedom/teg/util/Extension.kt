package com.adedom.teg.util

import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*

fun DateTime.toDateFormat() = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(toDate())

fun Int?.isNull() = this == null

fun Double?.isNull() = this == null

fun String.validateEmpty() = "Please enter $this"
