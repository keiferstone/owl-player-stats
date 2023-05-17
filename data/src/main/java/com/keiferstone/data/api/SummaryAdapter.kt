package com.keiferstone.data.api

import com.keiferstone.data.model.*
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader

class SummaryAdapter {
    @FromJson
    fun fromJson(
        jsonReader: JsonReader,
        playerJsonAdapter: JsonAdapter<PlayerSummary>,
        teamJsonAdapter: JsonAdapter<TeamSummary>): Summary {

        val players = mutableListOf<PlayerSummary>()
        val teams = mutableListOf<TeamSummary>()
        val matches = mutableListOf<MatchSummary>()
        val segments = mutableListOf<SegmentSummary>()

        jsonReader.beginObject()
        jsonReader.skipName()
        jsonReader.beginObject()

        while (jsonReader.hasNext()) {
            jsonReader.skipName()
            playerJsonAdapter.fromJson(jsonReader.nextSource().readUtf8())?.let {
                players.add(it)
            }
        }

        jsonReader.endObject()
        jsonReader.skipName()
        jsonReader.beginObject()

        while (jsonReader.hasNext()) {
            jsonReader.skipName()
            teamJsonAdapter.fromJson(jsonReader.nextSource().readUtf8())?.let {
                teams.add(it)
            }
        }

        jsonReader.endObject()
        jsonReader.skipName()
        jsonReader.beginObject()

        while (jsonReader.hasNext()) {
            jsonReader.skipName()
//            matchJsonAdapter.fromJson(jsonReader.nextSource().readUtf8())?.let {
//                matches.add(it)
//            }
            jsonReader.skipValue()
        }

        jsonReader.endObject()
        jsonReader.skipName()
        jsonReader.beginObject()

        while (jsonReader.hasNext()) {
            jsonReader.skipName()
//            segmentJsonAdapter.fromJson(jsonReader.nextSource().readUtf8())?.let {
//                segments.add(it)
//            }
            jsonReader.skipValue()
        }

        jsonReader.endObject()
        jsonReader.skipName()
        jsonReader.skipValue()
        jsonReader.skipName()
        jsonReader.skipValue()
        jsonReader.endObject()

        return Summary(
            players = players,
            teams = teams,
            matches = matches,
            segments = segments)
    }
}