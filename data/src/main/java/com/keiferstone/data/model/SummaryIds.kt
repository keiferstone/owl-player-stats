package com.keiferstone.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SummaryIds(
    val playerIds: List<Long>,
    val teamIds: List<Long>)
