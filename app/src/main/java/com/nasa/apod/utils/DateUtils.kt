package com.nasa.apod.utils

import java.text.SimpleDateFormat
import java.util.*

val dateFormatter = SimpleDateFormat("YYYY-MM-dd")

fun Date.today(): String = dateFormatter.format(this)
