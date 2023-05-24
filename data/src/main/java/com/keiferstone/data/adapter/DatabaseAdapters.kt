package com.keiferstone.data.adapter

import app.cash.sqldelight.ColumnAdapter

val listOfLongsAdapter = object : ColumnAdapter<List<Long>, String> {
    override fun decode(databaseValue: String): List<Long> {
        return runCatching {
            databaseValue.split(",").map { it.trim().toLong() }
        }.getOrElse { emptyList() }
    }

    override fun encode(value: List<Long>): String {
        return value.joinToString()
    }
}