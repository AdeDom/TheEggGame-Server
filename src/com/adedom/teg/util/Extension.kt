package com.adedom.teg.util

import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*

fun DateTime.toDateFormat() = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(toDate())
