package com.keiferstone.owlplayerstats.extension


import com.keiferstone.data.model.PlayerRoles
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
        trimmedFilter == Filter.PLAYS_ROLE + PlayerRoles.TANK -> Filter.PlaysRole(PlayerRoles.TANK)
        trimmedFilter == Filter.PLAYS_ROLE + PlayerRoles.DPS -> Filter.PlaysRole(PlayerRoles.DPS)
        trimmedFilter == Filter.PLAYS_ROLE + PlayerRoles.SUPPORT -> Filter.PlaysRole(PlayerRoles.SUPPORT)
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