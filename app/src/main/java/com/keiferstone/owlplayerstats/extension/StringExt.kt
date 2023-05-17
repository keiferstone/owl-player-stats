package com.keiferstone.owlplayerstats.extension

fun String.parseHexColor(): Int? {
    return runCatching {
        android.graphics.Color.parseColor("#$this")
    }.getOrNull()
}