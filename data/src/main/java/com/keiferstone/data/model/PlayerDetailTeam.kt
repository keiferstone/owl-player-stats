package com.keiferstone.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlayerDetailTeam(
    val id: Long,
    val logo: String?,
    val icon: String?,
    val primaryColor: String?,
    val secondaryColor: String?)