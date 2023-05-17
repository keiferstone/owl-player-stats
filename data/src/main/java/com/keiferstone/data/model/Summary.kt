package com.keiferstone.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Summary(
    val players: List<Player>,
    val teams: List<Team>,
    val matches: List<Match>,
    val segments: List<Segment>)
