package com.keiferstone.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Summary(
    val players: List<PlayerSummary>,
    val teams: List<TeamSummary>,
    val matches: List<MatchSummary>,
    val segments: List<SegmentSummary>)
