package com.keiferstone.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Team(
    val id: Long,
    val code: String,
    val name: String,
    val logo: String,
    val icon: String,
    val primaryColor: String,
    val secondaryColor: String)