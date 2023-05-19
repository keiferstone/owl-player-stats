package com.keiferstone.data.adapter

import app.cash.sqldelight.ColumnAdapter

val listOfLongsAdapter = object : ColumnAdapter<List<Long>, String> {
    override fun decode(databaseValue: String): List<Long> {
        return runCatching {
            databaseValue.split(",").map { it.toLong() }
        }.getOrElse { emptyList() }
    }

    override fun encode(value: List<Long>): String {
        return value.joinToString()
    }
}

val listOfStringsAdapter = object : ColumnAdapter<List<String>, String> {
    override fun decode(databaseValue: String): List<String> {
        return runCatching {
            databaseValue.split(",")
        }.getOrElse { emptyList() }
    }

    override fun encode(value: List<String>): String {
        return value.joinToString()
    }
}