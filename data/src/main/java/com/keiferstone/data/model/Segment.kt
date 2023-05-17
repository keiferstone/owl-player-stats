package com.keiferstone.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Segment(
    val id: String,
    val name: String?,
    val competitionId: String,
    val seasonId: String,
    val firstMatchStart: Long?,
    val lastMatchStart: Long?)