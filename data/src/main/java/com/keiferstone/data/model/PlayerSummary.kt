package com.keiferstone.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlayerSummary(
    val id: Long,
    val name: String,
    val number: Long,
    val role: String?,
    val preferredSlot: Long,
    val currentTeam: Long?,
    val givenName: String,
    val familyName: String,
    val headshotUrl: String?)