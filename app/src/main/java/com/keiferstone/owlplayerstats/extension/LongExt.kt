package com.keiferstone.owlplayerstats.extension

fun Long?.formatStatValue() = this?.let { "%,d".format(it) } ?: "??"