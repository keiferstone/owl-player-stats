package com.keiferstone.owlplayerstats.extension


import com.keiferstone.data.model.StatType
import com.keiferstone.owlplayerstats.exception.UnsupportedFilterIdException
import com.keiferstone.owlplayerstats.model.Filter

fun String.parseHexColor(): Int? {
    return runCatching {
        android.graphics.Color.parseColor("#$this")
    }.getOrNull()
}

fun String.parseFilter(): Filter {
    val trimmedFilter = trim()

    fun parseStatType() = StatType.valueOf(trimmedFilter.substringAfter(Filter.HAS_STAT))
    fun parseTimePlayed() = trimmedFilter.substringAfter(Filter.TIME_PLAYED).toLong()

    return when {
        trimmedFilter == Filter.ON_TEAM -> Filter.OnTeam
        trimmedFilter == Filter.TANK -> Filter.PlaysTank
        trimmedFilter == Filter.DPS -> Filter.PlaysDps
        trimmedFilter == Filter.SUPPORT -> Filter.PlaysSupport
        trimmedFilter == Filter.HAS_STATS -> Filter.HasStats
        trimmedFilter.startsWith(Filter.HAS_STAT) -> Filter.HasStat(parseStatType())
        trimmedFilter.startsWith(Filter.TIME_PLAYED) -> Filter.TimePlayed(parseTimePlayed())
        else -> throw UnsupportedFilterIdException(this)
    }
}

fun String?.parseFilterArgs(): List<Filter> {
    return this?.takeIf { it.isNotBlank() }
        ?.split(",")
        ?.map { it.parseFilter() }
        ?: emptyList()
}