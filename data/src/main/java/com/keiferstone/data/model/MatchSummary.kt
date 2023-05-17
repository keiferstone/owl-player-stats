package com.keiferstone.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MatchSummary(
    val id: String)